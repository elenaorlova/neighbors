package neighbors.service;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.entity.User;
import neighbors.enums.AdvertType;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.repository.AdvertRepository;
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
public class AdvertService {

    private final AdvertRepository advertRepository;
    private final UserRepository userRepository;

    public List<PartialBotApiMethod<? extends Serializable>> setDescription(User user, String description, String message) {
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        Advert advert = getAdvertByUser(user);
        setAdvertDescription(advert, user, description);
        user.setState(State.REGISTERED);
        userRepository.save(user);
        sendMessage.setText(message);
        SendMessage advertMessage = TelegramUtils.createMessageTemplate(user);
        advertMessage.setText(advert.getFullDescription());
        messages.add(sendMessage);
        messages.add(advertMessage);
        messages.addAll(MenuService.createMenu(user, Text.MAIN_MENU));
        return messages;
    }

    public Advert getAdvertByUser(User user) {
        return advertRepository.findAdvertByChatIdAndId(user.getChatId(), user.getCurrentAdvert());
    }

    public void setAdvertDescription(Advert advert, User user, String message) {
        advert.setDescription(message);
        advert.setFullDescription(buildAdvertMessage(user, advert));
        advertRepository.save(advert);
    }

    public void setAdvertName(User user, String name) {
        Advert advert = getAdvertByUser(user);
        advert.setName(name);
        advertRepository.save(advert);
    }

    public void setAdvertCategory(Advert advert, String category) {
        advert.setCategory(category.substring(1));
        advertRepository.save(advert);
    }

    public void setAdvertPrice(User user, String message) {
        Advert advert = advertRepository.findAdvertByChatIdAndId(user.getChatId(), user.getCurrentAdvert());
        advert.setPrice(Double.valueOf(message));
        advertRepository.save(advert);
    }

    public Advert saveAdvert(User user, AdvertType advertType) {
        Advert advert = new Advert();
        advert.setUsername(user.getUsername());
        advert.setAdvertType(advertType);
        advert.setChatId(user.getChatId());
        advert.setDistrict(user.getUserDistrict());
        advertRepository.save(advert);
        return advert;
    }

    private String buildAdvertMessage(User user, Advert advert) {
        return String.format(Text.ADVERT_TEXT,
                user.getUsername(),
                advert.getName(),
                advert.getPrice(),
                user.getUserDistrict().getName(),
                advert.getDescription()
        );
    }
}
