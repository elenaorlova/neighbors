package neighbors.service;

import neighbors.enums.bot.Text;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import neighbors.entity.BotUser;
import neighbors.enums.bot.Command;

import java.io.Serializable;
import java.util.List;

import static neighbors.utils.TelegramUtils.createButton;
import static neighbors.utils.TelegramUtils.createMessageTemplate;

@Service
public class MainService {

    public static List<PartialBotApiMethod<? extends Serializable>> createMainMenu(BotUser botUser) {
        SendMessage sendMessage = createMessageTemplate(botUser);
        sendMessage.setText(Text.MAIN_MENU.getText());
        sendMessage.setReplyMarkup(setUpInlineKeyboardMarkup());
        return List.of(sendMessage);
    }

    private static InlineKeyboardMarkup setUpInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(
                List.of(
                        createButton(Text.BUTTON_RENT.getText(), Command.RENT_OUT),
                        createButton(Text.BUTTON_RENT_OFF.getText(), Command.RENT))
        ));
        return inlineKeyboardMarkup;
    }
}
