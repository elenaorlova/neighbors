package neighbors.handler.notification;

import lombok.RequiredArgsConstructor;
import neighbors.enums.bot.Command;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.repository.BotUserRepository;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import neighbors.entity.BotUser;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SetNotificationHandler implements Handler {

    private final BotUserRepository botUserRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotUser botUser, String message) {
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(botUser);
        if (Command.ENABLE_RENT_NOTIFICATIONS.equals(message)) {
            sendMessage.setText(Text.ENABLING_NOTIFICATIONS.getText());
            sendMessage.setReplyMarkup(setUpInlineKeyboardMarkup());
            botUser.setSentNotifications(true);
            botUserRepository.save(botUser);
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
