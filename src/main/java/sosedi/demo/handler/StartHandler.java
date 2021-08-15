package sosedi.demo.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sosedi.demo.enums.State;
import sosedi.demo.entity.User;
import sosedi.demo.enums.Text;
import sosedi.demo.repository.UserRepository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static sosedi.demo.utils.TelegramUtils.createMessageTemplate;

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
