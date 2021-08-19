package neighbors.service;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.entity.BotUser;
import neighbors.enums.AdvertType;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.repository.AdvertRepository;
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
public class AdvertService {

    private final AdvertRepository advertRepository;
    private final BotUserRepository botUserRepository;
    private final NotificationService notificationService;

    public List<PartialBotApiMethod<? extends Serializable>> setDescription(BotUser botUser, String description,  String message) {
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(botUser);
        Advert advert = setAdvertDescription(botUser, description);
        botUser.setState(State.REGISTERED);
        botUserRepository.save(botUser);
        sendMessage.setText(message);
        SendMessage advertMessage = TelegramUtils.createMessageTemplate(botUser);
        advertMessage.setText(advert.getFullDescription());
        messages.add(sendMessage);
        messages.add(advertMessage);
        messages.addAll(MenuService.createMenu(botUser, Text.MAIN_MENU.getText()));
        messages.addAll(notificationService.createNotificationMessage(advert, botUser));
        return messages;
    }

    public Advert setAdvertDescription(BotUser botUser, String message) {
        Advert advert = advertRepository.findAdvertByChatIdAndId(botUser.getChatId(), botUser.getCurrentAdvert());
        advert.setDescription(message);
        advert.setFullDescription(buildAdvertMessage(botUser, advert));
        advertRepository.save(advert);
        return advert;
    }

    public void setAdvertPrice(BotUser botUser, String message) {
        Advert advert = advertRepository.findAdvertByChatIdAndId(botUser.getChatId(), botUser.getCurrentAdvert());
        advert.setPrice(Double.valueOf(message));
        advertRepository.save(advert);
    }

    public Advert saveAdvert(BotUser botUser, String message, AdvertType advertType) {
        Advert advert = new Advert();
        advert.setUsername(botUser.getUsername());
        advert.setAdvertType(advertType);
        advert.setChatId(botUser.getChatId());
        advert.setName(message.toLowerCase());
        advert.setDistrict(botUser.getUserDistrict());
        advertRepository.save(advert);
        return advert;
    }

    private String buildAdvertMessage(BotUser botUser, Advert advert) {
        return Text.ADVERT_TEXT.getText(
                botUser.getUsername(),
                advert.getName(),
                advert.getPrice(),
                botUser.getUserDistrict().getName(),
                advert.getDescription()
        );
    }
}
