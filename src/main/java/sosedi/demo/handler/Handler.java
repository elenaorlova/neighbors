package sosedi.demo.handler;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import sosedi.demo.enums.State;
import sosedi.demo.entity.User;

import java.io.Serializable;
import java.util.List;

public interface Handler {
    List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message);
    State operatedBotState();
    List<String> operatedCallBackQuery();
}
