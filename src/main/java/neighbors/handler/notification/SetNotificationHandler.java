package neighbors.handler.notification;

import lombok.RequiredArgsConstructor;
import neighbors.enums.NotificationCommand;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.repository.UserRepository;
import neighbors.service.MenuService;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import neighbors.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SetNotificationHandler implements Handler {

    private final UserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        if (NotificationCommand.ENABLE_RENT_NOTIFICATIONS.equals(message)) {
            sendMessage.setText(Text.ENABLING_NOTIFICATIONS.getText());
            sendMessage.setReplyMarkup(setUpInlineKeyboardMarkup());
            user.setSentNotifications(true);
            messages.add(sendMessage);
        } else {
            sendMessage.setText(Text.DISABLE_NOTIFICATIONS.getText());
            user.setState(State.REGISTERED);
            messages.add(sendMessage);
            messages.addAll(MenuService.createMenu(user, Text.MAIN_MENU.getText()));
        }
        userRepository.save(user);
        return messages;
    }

    private InlineKeyboardMarkup setUpInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(
                List.of(TelegramUtils.createButton(Text.BUTTON_ONLY_MY_DISTRICT.getText(), NotificationCommand.USER_DISTRICT_NOTIFICATIONS)),
                List.of(TelegramUtils.createButton(Text.BUTTON_SEVERAL_DISTRICTS.getText(), NotificationCommand.SEVERAL_DISTRICTS_NOTIFICATIONS)),
                List.of(TelegramUtils.createButton(Text.BUTTON_ALL_DISTRICTS.getText(), NotificationCommand.ALL_DISTRICTS_NOTIFICATIONS))
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
                NotificationCommand.ENABLE_RENT_NOTIFICATIONS,
                NotificationCommand.DISABLE_RENT_NOTIFICATIONS
        );
    }
}
