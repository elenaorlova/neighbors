package neighbors.handler.renting.out;

import lombok.RequiredArgsConstructor;
import neighbors.entity.User;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.service.AdvertService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentOutSetDescriptionHandler implements Handler {

    private final AdvertService advertService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        return advertService.setDescription(user, message, Text.CONFIRM_ADVERT);
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.RENTING_OUT_SET_DESCRIPTION);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
