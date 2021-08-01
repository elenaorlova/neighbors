package sosedi.demo.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sosedi.demo.enums.Command;
import sosedi.demo.enums.State;
import sosedi.demo.entity.User;
import sosedi.demo.repository.UserRepository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static sosedi.demo.utils.TelegramUtils.createButton;
import static sosedi.demo.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class RegistrationHandler implements Handler {

    private final UserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        user.setDistrict(message.toLowerCase());
        user.setState(State.DISTRICT_SELECTION);
        userRepository.save(user);
        SendMessage sendMessage = createMessageTemplate(user);
        sendMessage.setText("Хочешь ли ты получать оповещения о сдаче вещей?");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtons = List.of(
                createButton("Да", Command.ENABLE_RENT_NOTIFICATIONS),
                createButton("Нет", Command.DISABLE_RENT_NOTIFICATIONS)
        );
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtons));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return List.of(sendMessage);
    }

    @Override
    public State operatedBotState() {
        return State.REGISTRATION;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
