package sosedi.demo.handler.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sosedi.demo.entity.Notification;
import sosedi.demo.entity.NotificationDistrict;
import sosedi.demo.entity.User;
import sosedi.demo.enums.State;
import sosedi.demo.handler.Handler;
import sosedi.demo.repository.NotificationDistrictRepository;
import sosedi.demo.repository.NotificationRepository;
import sosedi.demo.repository.UserRepository;
import sosedi.demo.service.MainService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sosedi.demo.utils.TelegramUtils.createMessageTemplate;

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
        sendMessage.setText("Договорились! Буду отправлять оповещения о съеме вещей в этом районе");
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
    public State operatedBotState() {
        return State.NOTIFICATION_DISTRICT_SELECTION;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
