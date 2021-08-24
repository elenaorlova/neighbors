package neighbors.handler.notification;

import lombok.RequiredArgsConstructor;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.entity.District;
import neighbors.entity.User;
import neighbors.enums.bot.State;
import neighbors.repository.DistrictRepository;
import neighbors.repository.UserRepository;
import neighbors.service.MenuService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static neighbors.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class SeveralDistrictsNotificationHandler implements Handler {

    private final UserRepository userRepository;
    private final DistrictRepository districtRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = createMessageTemplate(user);
        String[] districtNames = message.replace(" ", "").split(",");
        List<District> notificationDistricts = new ArrayList<>();
        for (String districtName: districtNames) {
            notificationDistricts.add(new District(districtName));
        }
        user.setNotificationDistricts(notificationDistricts);
        sendMessage.setText(String.format(Text.NOTIFICATIONS_TURN_ON_IN_SEVERAL_DISTRICTS, notificationDistricts.stream()
                        .map(District::getName)
                        .collect(Collectors.toList())));
        user.setState(State.REGISTERED);
        districtRepository.saveAll(notificationDistricts);
        userRepository.save(user);
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        messages.add(sendMessage);
        messages.addAll(MenuService.createMenu(user, Text.MAIN_MENU));
        return messages;
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.NOTIFICATION_DISTRICT_SELECTION);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
