package sosedi.demo.handler.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sosedi.demo.entity.Advert;
import sosedi.demo.entity.User;
import sosedi.demo.enums.Text;
import sosedi.demo.enums.State;
import sosedi.demo.handler.Handler;
import sosedi.demo.service.MainService;
import sosedi.demo.service.RentService;
import sosedi.demo.utils.TelegramUtils;

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
