package neighbors.handler.renting;

import lombok.RequiredArgsConstructor;
import neighbors.entity.BotUser;
import neighbors.enums.AdvertType;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.repository.BotUserRepository;
import neighbors.service.AdvertService;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentHandler implements Handler {

    private final BotUserRepository botUserRepository;
    private final AdvertService advertService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotUser botUser, String message) {
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(botUser);
        botUser.setCurrentAdvert(advertService.saveAdvert(botUser, message, AdvertType.RENT).getId());
        botUser.setState(State.RENTING_SET_PRICE);
        botUserRepository.save(botUser);
        sendMessage.setText(Text.REQUEST_PRODUCT_PRICE.getText());
        return List.of(sendMessage);
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.RENTING);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
