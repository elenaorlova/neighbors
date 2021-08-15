package sosedi.demo.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sosedi.demo.enums.Command;
import sosedi.demo.enums.District;
import sosedi.demo.enums.State;
import sosedi.demo.entity.User;
import sosedi.demo.repository.UserRepository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static sosedi.demo.utils.TelegramUtils.createButton;
import static sosedi.demo.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class HomeDistrictHandler implements Handler {

    private final UserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        if (checkDistrict(message)) {
            user.setDistrict(message.toLowerCase());
        } else {
            SendMessage sendMessage = createMessageTemplate(user);
            sendMessage.setText("Не знаю про такой район. Может ты имел в виду какой-то из этих?"
                    + getSimilarDistricts(message));
            return List.of(sendMessage);
        }
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

    private List<String> getSimilarDistricts(String district) {
        String firstLetter = String.valueOf(district.charAt(0));
        return District.districts.stream()
                .filter(d -> d.startsWith(firstLetter))
                .collect(Collectors.toList());
    }

    private boolean checkDistrict(String district) {
        return District.districts.contains(district.toLowerCase());
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.REGISTRATION);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
