package neighbors.handler.renting;

import lombok.RequiredArgsConstructor;
import neighbors.entity.User;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.service.AdvertService;
import neighbors.service.NotificationService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentSetDescriptionHandler implements Handler {

    private final AdvertService advertService;
    private final NotificationService notificationService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        List<PartialBotApiMethod<? extends Serializable>> messages = advertService.setDescription(user, message, Text.CONFIRM_ADVERT.getText());
        messages.addAll(notificationService.createNotificationMessage(advertService.getAdvertByUser(user), user));
        return messages;
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
