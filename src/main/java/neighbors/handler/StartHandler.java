package neighbors.handler;

import lombok.RequiredArgsConstructor;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.entity.BotUser;
import neighbors.repository.BotUserRepository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static neighbors.utils.TelegramUtils.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class StartHandler implements Handler {

    private final BotUserRepository botUserRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotUser botUser, String message) {
        SendMessage welcomeMessage = createMessageTemplate(botUser);
        welcomeMessage.setText(Text.WELCOME_MESSAGE.getText());
        SendMessage districtMessage = createMessageTemplate(botUser);
        districtMessage.setText(Text.SELECT_USER_DISTRICT.getText());
        botUser.setState(State.REGISTRATION);
        botUserRepository.save(botUser);
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
