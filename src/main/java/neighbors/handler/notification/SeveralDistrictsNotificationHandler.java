package neighbors.handler.notification;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Notification;
import neighbors.enums.Text;
import neighbors.handler.Handler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.entity.NotificationDistrict;
import neighbors.entity.User;
import neighbors.enums.State;
import neighbors.repository.NotificationDistrictRepository;
import neighbors.repository.NotificationRepository;
import neighbors.repository.UserRepository;
import neighbors.service.MainService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static neighbors.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class SeveralDistrictsNotificationHandler implements Handler {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationDistrictRepository notificationDistrictRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = createMessageTemplate(user);
        Notification notification = notificationRepository.getNotificationByUserId(user.getId());
        String[] districts = message.split(",");
        List<NotificationDistrict> notificationDistricts = new ArrayList<>();
        for (String district: districts) {
            notificationDistricts.add(new NotificationDistrict(district));
        }
        notification.setNotificationDistricts(notificationDistricts);
        sendMessage.setText(Text.NOTIFICATIONS_TURN_ON_IN_SEVERAL_DISTRICTS.getText(
                notificationDistricts.stream()
                        .map(NotificationDistrict::getDistrict)
                        .collect(Collectors.toList())
                        .toString()
                )
        );
        user.setState(State.REGISTERED);
        userRepository.save(user);
        notificationDistrictRepository.saveAll(notificationDistricts);
        notificationRepository.save(notification);
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        messages.add(sendMessage);
        messages.addAll(MainService.createMainMenu(user));
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
