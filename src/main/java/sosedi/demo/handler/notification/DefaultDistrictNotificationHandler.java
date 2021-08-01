package sosedi.demo.handler.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sosedi.demo.entity.Notification;
import sosedi.demo.entity.NotificationDistrict;
import sosedi.demo.entity.User;
import sosedi.demo.enums.Command;
import sosedi.demo.enums.State;
import sosedi.demo.handler.Handler;
import sosedi.demo.repository.NotificationRepository;
import sosedi.demo.repository.UserRepository;

import java.io.Serializable;
import java.util.List;

import static sosedi.demo.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class DefaultDistrictNotificationHandler implements Handler {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = createMessageTemplate(user);
        Notification notification = notificationRepository.getNotificationByUserId(user.getId());
        user.setState(State.REGISTERED);
        if (Command.USER_DISTRICT_NOTIFICATIONS.equals(message)) {
            notification.setNotificationDistricts(List.of(new NotificationDistrict(user.getDistrict())));
            sendMessage.setText("Договорились! Буду отправлять оповещения о съёме в твоём районе");
        } if (Command.SEVERAL_DISTRICTS_NOTIFICATIONS.equals(message)) {
            sendMessage.setText("Введи название районов через запятую");
            user.setState(State.NOTIFICATION_DISTRICT_SELECTION);
        } else {
            notification.setNotificationDistricts(List.of(new NotificationDistrict("all")));
            sendMessage.setText("Договорились! Буду отправлять оповещения обо всех объявлениях о съёме");
        }
        userRepository.save(user);
        notificationRepository.save(notification);
        return List.of(sendMessage);
    }

    @Override
    public State operatedBotState() {
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
