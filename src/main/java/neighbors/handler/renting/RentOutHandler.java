package neighbors.handler.renting;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.entity.BotUser;
import neighbors.enums.AdvertType;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.repository.AdvertRepository;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.repository.BotUserRepository;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentOutHandler implements Handler {

    private final AdvertRepository advertRepository;
    private final BotUserRepository botUserRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotUser botUser, String message) {
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(botUser);
        Advert advert = new Advert();
        advert.setUsername(botUser.getUsername());
        advert.setAdvertType(AdvertType.RENT_OFF);
        advert.setChatId(botUser.getChatId());
        advert.setName(message.toLowerCase());
        advert.setDistrict(botUser.getUserDistrict());
        advertRepository.save(advert);
        botUser.setCurrentAdvert(advert.getId());
        botUser.setState(State.RENTING_OUT_SET_PRICE);
        botUserRepository.save(botUser);
        sendMessage.setText(Text.REQUEST_PRODUCT_PRICE.getText());
        return List.of(sendMessage);
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.RENTING_OUT);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
