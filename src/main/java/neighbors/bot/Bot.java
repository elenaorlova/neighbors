package neighbors.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neighbors.UpdateReceiver;
import neighbors.enums.State;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private final UpdateReceiver updateReceiver;

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    private final UserRepository userRepository;

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

    private void executeWithExceptionCheck(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error when sending message: {}, {}", sendMessage.getText(), e.getMessage());
        }
    }
}

