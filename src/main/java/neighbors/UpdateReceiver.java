package neighbors;

import lombok.RequiredArgsConstructor;
import neighbors.handler.Handler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import neighbors.enums.bot.State;
import neighbors.entity.BotUser;
import neighbors.repository.BotUserRepository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateReceiver {

    private final List<Handler> handlers;
    private final BotUserRepository botUserRepository;

    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        try {
            if (isMessageWithText(update)) {
                Message message = update.getMessage();
                Long chatId = message.getFrom().getId();
                BotUser botUser = getUser(message, chatId);
                return getHandlerByState(botUser.getState()).handle(botUser, message.getText());
            } else if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                Long chatId = callbackQuery.getFrom().getId();
                BotUser botUser = getUser(callbackQuery.getMessage(), chatId);
                return getHandlerByCallBackQuery(callbackQuery.getData()).handle(botUser, callbackQuery.getData());
            }
            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            return Collections.emptyList();
        }
    }

    private BotUser getUser(Message message, Long chatId) {
        return botUserRepository.getByChatId(chatId).orElseGet(() -> botUserRepository.save(new BotUser(chatId, message.getFrom().getUserName())));
    }

    private Handler getHandlerByState(State state) {
        return handlers.stream()
                .filter(handler -> handler.operatedBotState() != null)
                .filter(handler -> handler.operatedBotState().stream().anyMatch(state::equals))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(String query) {
        return handlers.stream()
                .filter(handler -> handler.operatedCallBackQuery() != null)
                .filter(handler -> handler.operatedCallBackQuery().stream().anyMatch(query::startsWith))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
