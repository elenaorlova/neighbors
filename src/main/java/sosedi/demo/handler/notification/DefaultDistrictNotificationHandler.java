package sosedi.demo.handler.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sosedi.demo.entity.Notification;
import sosedi.demo.entity.NotificationDistrict;
import sosedi.demo.entity.User;
import sosedi.demo.enums.Command;
import sosedi.demo.enums.Text;
import sosedi.demo.enums.State;
import sosedi.demo.handler.Handler;
import sosedi.demo.service.MainService;
import sosedi.demo.repository.NotificationDistrictRepository;
import sosedi.demo.repository.NotificationRepository;
import sosedi.demo.repository.UserRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sosedi.demo.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class DefaultDistrictNotificationHandler implements Handler {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationDistrictRepository notificationDistrictRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = createMessageTemplate(user);
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        user.setState(State.REGISTERED);
        if (Command.SEVERAL_DISTRICTS_NOTIFICATIONS.equals(message)) {
            sendMessage.setText(Text.SELECTING_SEVERAL_AREAS_FOR_NOTIFICATIONS.getText());
            user.setState(State.NOTIFICATION_DISTRICT_SELECTION);
            messages.add(sendMessage);
        } else {
            Notification notification = notificationRepository.getNotificationByUserId(user.getId());
            NotificationDistrict notificationDistrict;
            if (Command.USER_DISTRICT_NOTIFICATIONS.equals(message)) {
                notificationDistrict = new NotificationDistrict(user.getDistrict());
                sendMessage.setText(Text.NOTIFICATIONS_TURN_ON_IN_USER_DISTRICT.getText());
            }  else {
                notificationDistrict = new NotificationDistrict("all");
                sendMessage.setText(Text.NOTIFICATIONS_TURN_ON_IN_ALL_DISTRICTS.getText());
            }
            notification.setNotificationDistricts(List.of(notificationDistrict));
            notificationDistrictRepository.save(notificationDistrict);
            notificationRepository.save(notification);
            messages.add(sendMessage);
            messages.addAll(MainService.createMainMenu(user));
        }
        userRepository.save(user);
        return messages;
    }

    @Override
    public List<State> operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(
                Command.USER_DISTRICT_NOTIFICATIONS,
                Command.SEVERAL_DISTRICTS_NOTIFICATIONS,
                Command.ALL_DISTRICTS_NOTIFICATIONS
        );
    }
}
