package neighbors.handler.notification;

import lombok.RequiredArgsConstructor;
import neighbors.enums.bot.Command;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.repository.UserRepository;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import neighbors.entity.User;

import java.io.Serializable;
import java.util.List;

import static neighbors.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class NotificationHandler implements Handler {

    private final UserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        if (Command.ENABLE_RENT_NOTIFICATIONS.equals(message)) {
            sendMessage.setText(Text.ENABLING_NOTIFICATIONS.getText());
            sendMessage.setReplyMarkup(setUpInlineKeyboardMarkup());
            user.setSentNotifications(true);
            userRepository.save(user);
        } else {
            sendMessage.setText(Text.DISABLE_NOTIFICATIONS.getText());
        }
        return List.of(sendMessage);
    }

    private InlineKeyboardMarkup setUpInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(
                List.of(TelegramUtils.createButton(Text.BUTTON_ONLY_MY_DISTRICT.getText(), Command.USER_DISTRICT_NOTIFICATIONS)),
                List.of(TelegramUtils.createButton(Text.BUTTON_SEVERAL_DISTRICTS.getText(), Command.SEVERAL_DISTRICTS_NOTIFICATIONS)),
                List.of(TelegramUtils.createButton(Text.BUTTON_ALL_DISTRICTS.getText(), Command.ALL_DISTRICTS_NOTIFICATIONS))
        ));
        return inlineKeyboardMarkup;
    }

    @Override
    public List<State> operatedBotState() {
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
