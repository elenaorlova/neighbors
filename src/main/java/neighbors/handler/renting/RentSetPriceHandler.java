package neighbors.handler.renting;

import lombok.RequiredArgsConstructor;
import neighbors.entity.User;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.repository.UserRepository;
import neighbors.service.AdvertService;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentSetPriceHandler implements Handler {

    private final UserRepository userRepository;
    private final AdvertService advertService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        advertService.setAdvertPrice(user, message);
        sendMessage.setText(Text.REQUEST_PRODUCT_DESCRIPTION);
        user.setState(State.RENTING_SET_DESCRIPTION);
        userRepository.save(user);
        return List.of(sendMessage);
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.RENTING_SET_PRICE);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
