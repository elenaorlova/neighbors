package neighbors.handler;

import lombok.RequiredArgsConstructor;
import neighbors.enums.State;
import neighbors.enums.Text;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.entity.User;
import neighbors.repository.UserRepository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static neighbors.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class StartHandler implements Handler {

    private final UserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage welcomeMessage = createMessageTemplate(user);
        welcomeMessage.setText(Text.WELCOME_MESSAGE.getText());
        SendMessage districtMessage = createMessageTemplate(user);
        districtMessage.setText(Text.SELECT_USER_DISTRICT.getText());
        user.setState(State.REGISTRATION);
        userRepository.save(user);
        return List.of(welcomeMessage, districtMessage);
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.NEW_USER);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
