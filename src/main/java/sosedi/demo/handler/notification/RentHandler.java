package sosedi.demo.handler.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sosedi.demo.entity.Objects;
import sosedi.demo.entity.User;
import sosedi.demo.enums.State;
import sosedi.demo.handler.Handler;
import sosedi.demo.service.MainService;
import sosedi.demo.service.RentService;
import sosedi.demo.utils.TelegramUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentHandler implements Handler {

    private final RentService rentService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        List<Objects> objects = rentService.findAllObjectsByName(message);
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        if (objects.isEmpty()) {
            sendMessage.setText("К сожалению, такие товары пока никто не сдаёт");
        } else {
            sendMessage.setText("Я нашел для тебя следующие товары: " + objects);
            // todo: objects should be constructed
        }
        messages.add(sendMessage);
        messages.addAll(MainService.createMainMenu(user));
        return messages;
    }

    @Override
    public State operatedBotState() {
        return State.RENTING;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
