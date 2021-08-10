package sosedi.demo.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sosedi.demo.entity.User;
import sosedi.demo.enums.Command;
import sosedi.demo.enums.State;
import sosedi.demo.repository.UserRepository;
import sosedi.demo.utils.TelegramUtils;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MainMenuHandler implements Handler {

    private final UserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        if (Command.RENT.equals(message)) {
            sendMessage.setText("Какой товар вы хотите снять? Введите название (например, дрель)");
            user.setState(State.RENTING);
        } else if (Command.RENT_OUT.equals(message)) {
            sendMessage.setText("Введите название товара");
            user.setState(State.RENTING_OUT);
        }
        userRepository.save(user);
        return List.of(sendMessage);
    }

    @Override
    public State operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(Command.RENT, Command.RENT_OUT);
    }
}
