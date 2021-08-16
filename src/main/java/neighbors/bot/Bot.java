package neighbors.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neighbors.UpdateReceiver;
import neighbors.entity.Notification;
import neighbors.enums.NotificationStatus;
import neighbors.enums.bot.State;
import neighbors.repository.NotificationRepository;
import neighbors.utils.TelegramUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import neighbors.entity.User;
import neighbors.repository.UserRepository;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private final UpdateReceiver updateReceiver;
    private static final List<String> SEND_STATUSES = List.of(NotificationStatus.CREATED.name(), NotificationStatus.ERROR.name());

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Scheduled(fixedDelay = 3000)
    public void sendNotifications() {
        List<Notification> notifications = notificationRepository.findAllByStatusIn(SEND_STATUSES);
        notifications.forEach(
                notification -> {
                    SendMessage sendMessage = TelegramUtils.createMessageTemplate(notification.getChatId());
                    sendMessage.setText(notification.getAdvert().getFullDescription());
                    if (executeWithExceptionCheck(sendMessage)) {
                        notification.setStatus(NotificationStatus.SENT.name());
                    } else {
                        notification.setStatus(NotificationStatus.ERROR.name());
                    }
                }
        );
        notificationRepository.saveAll(notifications);
    }

    public void onState(User user, List<PartialBotApiMethod<? extends Serializable>> messagesToSend) {
        if (messagesToSend != null && !messagesToSend.isEmpty()) {
            messagesToSend.forEach(response -> {
                if (response instanceof SendMessage) {
                    executeWithExceptionCheck((SendMessage) response);
                }
            });
        }
        user.setState(State.NOTIFICATIONS_ON);
        userRepository.save(user);
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = updateReceiver.handle(update);
        if (messagesToSend != null && !messagesToSend.isEmpty()) {
            messagesToSend.forEach(response -> {
                if (response instanceof SendMessage) {
                    executeWithExceptionCheck((SendMessage) response);
                }
            });
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private boolean executeWithExceptionCheck(SendMessage sendMessage) {
        try {
            execute(sendMessage);
            return true;
        } catch (TelegramApiException e) {
            log.error("Error when sending message: {}, {}", sendMessage.getText(), e.getMessage());
            return false;
        }
    }
}

