package neighbors.handler.notification;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.entity.BotUser;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.repository.AdvertRepository;
import neighbors.repository.BotUserRepository;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentingOutSetPriceHandler implements Handler {

    private final AdvertRepository advertRepository;
    private final BotUserRepository botUserRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotUser botUser, String message) {
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(botUser);
        Advert advert = advertRepository.findAdvertByChatIdAndId(botUser.getChatId(), botUser.getCurrentAdvert());
        advert.setPrice(Double.valueOf(message));
        sendMessage.setText(Text.REQUEST_PRODUCT_DESCRIPTION.getText());
        botUser.setState(State.RENTING_OUT_SET_DESCRIPTION);
        advertRepository.save(advert);
        botUserRepository.save(botUser);
        return List.of(sendMessage);
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.RENTING_OUT_SET_PRICE);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
