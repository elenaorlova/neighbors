package neighbors.service;

import neighbors.enums.MainCommand;
import neighbors.enums.bot.Text;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import neighbors.entity.User;

import java.io.Serializable;
import java.util.List;

import static neighbors.utils.TelegramUtils.createButton;
import static neighbors.utils.TelegramUtils.createMessageTemplate;

@Service
public class MenuService {

    public static List<PartialBotApiMethod<? extends Serializable>> createMenu(User user, String message) {
        SendMessage sendMessage = createMessageTemplate(user);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(setUpInlineKeyboardMarkup());
        return List.of(sendMessage);
    }

    private static InlineKeyboardMarkup setUpInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(
                List.of(createButton(Text.BUTTON_RENT, MainCommand.RENT), createButton(Text.BUTTON_RENT_OUT, MainCommand.RENT_OUT)),
                List.of(createButton("Посмотреть мои объявления", MainCommand.GET_MY_ADVERTS)),
                List.of(createButton("Посмотреть все объявления о сдаче", MainCommand.GET_RENT_OUT_ADVERTS)),
                List.of(createButton("Посмотреть все объявления о съеме", MainCommand.GET_RENT_ADVERTS))
        ));
        return inlineKeyboardMarkup;
    }
}
