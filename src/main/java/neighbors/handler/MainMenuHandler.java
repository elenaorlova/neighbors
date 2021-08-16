package neighbors.handler;

import lombok.RequiredArgsConstructor;
import neighbors.enums.bot.Command;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.entity.User;
import neighbors.repository.UserRepository;

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
