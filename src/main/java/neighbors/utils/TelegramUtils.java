package neighbors.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import neighbors.entity.BotUser;

public class TelegramUtils {

    public static SendMessage createMessageTemplate(BotUser botUser) {
        return createMessageTemplate(String.valueOf(botUser.getChatId()));
    }

    public static SendMessage createMessageTemplate(Long chatId) {
        return createMessageTemplate(String.valueOf(chatId));
    }

    public static SendMessage createMessageTemplate(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }

    public static InlineKeyboardButton createButton(String text, String command) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(command);
        return inlineKeyboardButton;
    }

}
