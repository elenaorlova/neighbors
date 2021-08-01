package sosedi.demo.handler.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import sosedi.demo.entity.Notification;
import sosedi.demo.entity.User;
import sosedi.demo.enums.Command;
import sosedi.demo.enums.State;
import sosedi.demo.handler.Handler;
import sosedi.demo.repository.NotificationRepository;

import java.io.Serializable;
import java.util.List;

import static sosedi.demo.utils.TelegramUtils.createButton;
import static sosedi.demo.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class NotificationHandler implements Handler {

    private final NotificationRepository notificationRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        Notification notification = new Notification(user.getId(), false);
        SendMessage sendMessage = createMessageTemplate(user);
        if (Command.ENABLE_RENT_NOTIFICATIONS.equals(message)) {
            sendMessage.setText("Об объявлениях в каком районе ты хочешь получать уведомления?");
            sendMessage.setReplyMarkup(setUpInlineKeyboardMarkup());
            notification.setEnabled(true);
        } else {
            sendMessage.setText("Окей, не буду отправлять тебе уведомления.");
        }
        notificationRepository.save(notification);
        return List.of(sendMessage);
    }

    private InlineKeyboardMarkup setUpInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(
                List.of(createButton("Только в своём", Command.USER_DISTRICT_NOTIFICATIONS)),
                List.of(createButton("Выбрать несколько", Command.SEVERAL_DISTRICTS_NOTIFICATIONS)),
                List.of(createButton("Во всех", Command.ALL_DISTRICTS_NOTIFICATIONS))
        ));
        return inlineKeyboardMarkup;
    }

    @Override
    public State operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(
                Command.ENABLE_RENT_NOTIFICATIONS,
                Command.DISABLE_RENT_NOTIFICATIONS
        );
    }
}
