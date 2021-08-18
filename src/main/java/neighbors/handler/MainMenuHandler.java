package neighbors.handler;

import lombok.RequiredArgsConstructor;
import neighbors.enums.bot.Command;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.entity.BotUser;
import neighbors.repository.BotUserRepository;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MainMenuHandler implements Handler {

    private final BotUserRepository botUserRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotUser botUser, String message) {
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(botUser);
        if (Command.RENT.equals(message)) {
            sendMessage.setText(Text.REQUEST_RENTING_NAME.getText());
            botUser.setState(State.RENTING);
        } else if (Command.RENT_OUT.equals(message)) {
            sendMessage.setText(Text.REQUEST_RENTING_OUT_NAME.getText());
            botUser.setState(State.RENTING_OUT);
        }
        botUserRepository.save(botUser);
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
