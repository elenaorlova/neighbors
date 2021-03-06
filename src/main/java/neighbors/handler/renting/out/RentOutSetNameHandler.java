package neighbors.handler.renting.out;

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
public class RentOutSetNameHandler implements Handler {

    private final UserRepository userRepository;
    private final AdvertService advertService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        advertService.setAdvertName(user, message);
        sendMessage.setText(Text.REQUEST_PRODUCT_PRICE);
        user.setState(State.RENTING_OUT_SET_PRICE);
        userRepository.save(user);
        return List.of(sendMessage);
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.RENTING_OUT_SET_NAME);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
