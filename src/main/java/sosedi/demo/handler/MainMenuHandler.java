package sosedi.demo.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sosedi.demo.entity.User;
import sosedi.demo.enums.Command;
import sosedi.demo.enums.State;
import sosedi.demo.enums.Text;
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
            sendMessage.setText(Text.REQUEST_RENTING_NAME.getText());
            user.setState(State.RENTING);
        } else if (Command.RENT_OUT.equals(message)) {
            sendMessage.setText(Text.REQUEST_RENTING_OUT_NAME.getText());
            user.setState(State.RENTING_OUT);
        }
        userRepository.save(user);
        return List.of(sendMessage);
    }

    @Override
    public List<State> operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(Command.RENT, Command.RENT_OUT);
    }
}
