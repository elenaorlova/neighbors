package neighbors.service;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.entity.User;
import neighbors.repository.UserRepository;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;

    public List<PartialBotApiMethod<? extends Serializable>> sendNotification(Advert advert, User currentUser) {
        List<User> users = userRepository.findAllBySentNotificationsAndNotificationDistrictsContains(true, advert.getDistrict().getName());
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        users.remove(currentUser);
        users.forEach( user -> {
            SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
            sendMessage.setText(advert.getFullDescription());
            messages.add(sendMessage);
        });
        return messages;
    }
}
