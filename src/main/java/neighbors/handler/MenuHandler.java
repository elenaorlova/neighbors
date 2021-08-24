package neighbors.handler;

import lombok.RequiredArgsConstructor;
import neighbors.enums.CategoryCommand;
import neighbors.enums.MainCommand;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.entity.User;
import neighbors.repository.UserRepository;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.Serializable;
import java.util.List;

import static neighbors.utils.TelegramUtils.createButton;

@Component
@RequiredArgsConstructor
public class MenuHandler implements Handler {

    private final UserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        if (MainCommand.RENT.equals(message)) {
            sendMessage.setText("Выберите категорию товара");
            sendMessage.setReplyMarkup(setUpInlineKeyboardMarkup());
            user.setState(State.RENTING_SELECT_CATEGORY);
        } else if (MainCommand.RENT_OUT.equals(message)) {
            sendMessage.setText("Выберите категорию товара");
            sendMessage.setReplyMarkup(setUpInlineKeyboardMarkup());
            user.setState(State.RENTING_OUT_SELECT_CATEGORY);
        } else if (MainCommand.GET_MY_ADVERTS.equals(message)) {
            sendMessage.setText(Text.REQUEST_RENTING_OUT_NAME);
            user.setState(State.GET_MY_ADVERTS);
        } else if (MainCommand.GET_RENT_OUT_ADVERTS.equals(message)) {
            sendMessage.setText(Text.REQUEST_RENTING_OUT_NAME);
            user.setState(State.GET_RENT_OUT_ADVERTS);
        } else if (MainCommand.GET_RENT_ADVERTS.equals(message)) {
            sendMessage.setText(Text.REQUEST_RENTING_OUT_NAME);
            user.setState(State.GET_RENT_ADVERTS);
        }
        userRepository.save(user);
        return List.of(sendMessage);
    }

    private InlineKeyboardMarkup setUpInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(
                List.of(createButton("Ремонт", CategoryCommand.REPAIR_THINGS)),
                List.of(createButton("Одежда", CategoryCommand.CLOTHES)),
                List.of(createButton("Другое", CategoryCommand.OTHER))
        ));
        return inlineKeyboardMarkup;
    }

    @Override
    public List<State> operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(
                MainCommand.RENT,
                MainCommand.RENT_OUT,
                MainCommand.GET_MY_ADVERTS,
                MainCommand.GET_RENT_OUT_ADVERTS,
                MainCommand.GET_RENT_ADVERTS
        );
    }
}
