package sosedi.demo.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import sosedi.demo.entity.User;
import sosedi.demo.enums.Command;
import sosedi.demo.enums.State;

import java.io.Serializable;
import java.util.List;

import static sosedi.demo.utils.TelegramUtils.createButton;
import static sosedi.demo.utils.TelegramUtils.createMessageTemplate;

@Component
public class WelcomeHandler implements Handler {

    // todo: send message immediately, not on update

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = createMessageTemplate(user);
        sendMessage.setText("Ты успешно прошел регистрацию, теперь тебе доступен мой функционал!");
        SendMessage sendMessage1 = createMessageTemplate(user);
        sendMessage1.setText("Хочешь арендовать товар или зашерить свой?");
        sendMessage1.setReplyMarkup(setUpInlineKeyboardMarkup());
        return List.of(sendMessage, sendMessage1);
    }

    private InlineKeyboardMarkup setUpInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(
                List.of(createButton("Сдать", Command.RENT), createButton("Снять", Command.RENT_OUT))
        ));
        return inlineKeyboardMarkup;
    }

    @Override
    public State operatedBotState() {
        return State.REGISTERED;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
