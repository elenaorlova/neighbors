package neighbors.handler.notification;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.entity.User;
import neighbors.enums.AdvertType;
import neighbors.enums.State;
import neighbors.enums.Text;
import neighbors.handler.Handler;
import neighbors.repository.AdvertRepository;
import neighbors.service.MainService;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.repository.UserRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentingOutHandler implements Handler {

    private final AdvertRepository advertRepository;
    private final UserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        if (user.getCurrentAdvert() == null) {
            Advert advert = new Advert();
            advert.setUsername(user.getUsername());
            advert.setAdvertType(AdvertType.RENT_OFF);
            advert.setUserId(user.getId());
            advert.setName(message);
            advertRepository.save(advert);
            user.setCurrentAdvert(advert.getId());
            userRepository.save(user);
            sendMessage.setText(Text.REQUEST_PRODUCT_PRICE.getText());
            return List.of(sendMessage);
        }
        Advert advert = advertRepository.findAdvertByUserIdAndId(user.getId(), user.getCurrentAdvert());
        if (advert.getPrice() == null) {
            advert.setPrice(Double.valueOf(message));
            sendMessage.setText(Text.REQUEST_PRODUCT_DESCRIPTION.getText());
            advertRepository.save(advert);
            messages.add(sendMessage);
        } else if (advert.getDescription() == null) {
            advert.setDescription(message);
            String advertText = buildAdvertMessage(user, advert);
            advert.setFullDescription(advertText);
            advertRepository.save(advert);
            user.setState(State.REGISTERED);
            sendMessage.setText(Text.CONFIRM_ADVERT.getText());
            SendMessage advertMessage = TelegramUtils.createMessageTemplate(user);
            advertMessage.setText(advertText);
            messages.add(sendMessage);
            messages.add(advertMessage);
            messages.addAll(MainService.createMainMenu(user));
        }
        return messages;
    }

    private String buildAdvertMessage(User user, Advert advert) {
        return Text.ADVERT_TEXT.getText(user.getUsername(), advert.getName(), advert.getPrice(), advert.getDescription());
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.RENTING_OUT, State.RENTING_OUT_SET_PRICE);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
