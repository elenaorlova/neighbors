package neighbors.handler.notification;

import lombok.RequiredArgsConstructor;
import neighbors.entity.District;
import neighbors.enums.NotificationCommand;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.repository.DistrictRepository;
import neighbors.service.MenuService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.entity.BotUser;
import neighbors.repository.BotUserRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static neighbors.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class DefaultDistrictNotificationHandler implements Handler {

    private final BotUserRepository botUserRepository;
    private final DistrictRepository districtRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotUser botUser, String message) {
        SendMessage sendMessage = createMessageTemplate(botUser);
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        botUser.setState(State.REGISTERED);
        if (NotificationCommand.SEVERAL_DISTRICTS_NOTIFICATIONS.equals(message)) {
            sendMessage.setText(Text.SELECTING_SEVERAL_AREAS_FOR_NOTIFICATIONS.getText());
            botUser.setState(State.NOTIFICATION_DISTRICT_SELECTION);
            messages.add(sendMessage);
        } else {
            District district;
            if (NotificationCommand.USER_DISTRICT_NOTIFICATIONS.equals(message)) {
                district = botUser.getUserDistrict();
                sendMessage.setText(Text.NOTIFICATIONS_TURN_ON_IN_USER_DISTRICT.getText());
            }  else {
                district = new District("all");
                sendMessage.setText(Text.NOTIFICATIONS_TURN_ON_IN_ALL_DISTRICTS.getText());
            }
            botUser.setNotificationDistricts(List.of(district));
            districtRepository.save(district);
            messages.add(sendMessage);
            messages.addAll(MenuService.createMenu(botUser, Text.MAIN_MENU.getText()));
        }
        botUserRepository.save(botUser);
        return messages;
    }

    @Override
    public List<State> operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(
                NotificationCommand.USER_DISTRICT_NOTIFICATIONS,
                NotificationCommand.SEVERAL_DISTRICTS_NOTIFICATIONS,
                NotificationCommand.ALL_DISTRICTS_NOTIFICATIONS
        );
    }
}
