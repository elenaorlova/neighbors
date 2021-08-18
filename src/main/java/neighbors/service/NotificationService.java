package neighbors.service;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.entity.BotUser;
import neighbors.repository.BotUserRepository;
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

    private final BotUserRepository botUserRepository;

    public List<PartialBotApiMethod<? extends Serializable>> sendNotification(Advert advert, BotUser currentBotUser) {
        List<BotUser> botUsers = botUserRepository.findAllBySentNotificationsAndNotificationDistrictsContains(true, advert.getDistrict().getName());
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        botUsers.remove(currentBotUser);
        botUsers.forEach(user -> {
            SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
            sendMessage.setText(advert.getFullDescription());
            messages.add(sendMessage);
        });
        return messages;
    }
}
