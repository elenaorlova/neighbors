package neighbors.handler;

import lombok.RequiredArgsConstructor;
import neighbors.entity.District;
import neighbors.enums.bot.Text;
import neighbors.repository.DistrictRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import neighbors.enums.bot.Command;
import neighbors.enums.DistrictEnum;
import neighbors.enums.bot.State;
import neighbors.entity.BotUser;
import neighbors.repository.BotUserRepository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static neighbors.utils.TelegramUtils.createButton;
import static neighbors.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class HomeDistrictHandler implements Handler {

    private final BotUserRepository botUserRepository;
    private final DistrictRepository districtRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotUser botUser, String message) {
        if (checkDistrict(message)) {
            botUser.setUserDistrict(new District(message.toLowerCase()));
        } else {
            SendMessage sendMessage = createMessageTemplate(botUser);
            sendMessage.setText(Text.DISTRICT_NOT_FOUND.getText(getSimilarDistricts(message)));
            return List.of(sendMessage);
        }
        botUser.setState(State.DISTRICT_SELECTION);
        districtRepository.save(botUser.getUserDistrict());
        botUserRepository.save(botUser);
        SendMessage sendMessage = createMessageTemplate(botUser);
        sendMessage.setText(Text.REQUEST_TO_ENABLE_NOTIFICATIONS.getText());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtons = List.of(
                createButton(Text.USER_ENABLE_NOTIFICATIONS.getText(), Command.ENABLE_RENT_NOTIFICATIONS),
                createButton(Text.USER_DISABLE_NOTIFICATIONS.getText(), Command.DISABLE_RENT_NOTIFICATIONS)
        );
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtons));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return List.of(sendMessage);
    }

    private List<String> getSimilarDistricts(String district) {
        String firstLetter = String.valueOf(district.charAt(0));
        return DistrictEnum.districts.stream()
                .filter(d -> d.startsWith(firstLetter))
                .collect(Collectors.toList());
    }

    private boolean checkDistrict(String district) {
        return DistrictEnum.districts.contains(district.toLowerCase());
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
