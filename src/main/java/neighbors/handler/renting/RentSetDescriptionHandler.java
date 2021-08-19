package neighbors.handler.renting;

import lombok.RequiredArgsConstructor;
import neighbors.entity.BotUser;
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
public class RentSetDescriptionHandler implements Handler {

    private final AdvertService advertService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotUser botUser, String message) {
        return advertService.setDescription(botUser, message, Text.CONFIRM_ADVERT.getText());
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.RENTING_SET_DESCRIPTION);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
