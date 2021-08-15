package neighbors.handler;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import neighbors.enums.State;
import neighbors.entity.User;

import java.io.Serializable;
import java.util.List;

public interface Handler {
    List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message);
    List<State> operatedBotState();
    List<String> operatedCallBackQuery();
}
