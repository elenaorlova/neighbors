package neighbors.handler.notification;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.enums.State;
import neighbors.enums.Text;
import neighbors.handler.Handler;
import neighbors.service.MainService;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.entity.User;
import neighbors.service.RentService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RentingHandler implements Handler {

    private final RentService rentService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        List<Advert> adverts = rentService.findAllObjectsByName(message);
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        if (adverts.isEmpty()) {
            sendMessage.setText(Text.NOT_FOUND_ADVERTS.getText(message));
        } else {
            sendMessage.setText(Text.ADVERTS_FOUND.getText(
                    adverts.stream()
                            .map(Advert::getFullDescription)
                            .collect(Collectors.toList()).toString()
                    )
            );
        }
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        messages.add(sendMessage);
        messages.addAll(MainService.createMainMenu(user));
        return messages;
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
