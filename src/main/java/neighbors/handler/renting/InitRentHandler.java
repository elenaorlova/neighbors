package neighbors.handler.renting;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.enums.RentCommand;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.repository.UserRepository;
import neighbors.service.MenuService;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import neighbors.entity.User;
import neighbors.service.RentService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static neighbors.utils.TelegramUtils.createButton;

@Component
@RequiredArgsConstructor
public class InitRentHandler implements Handler {

    private final RentService rentService;
    private final UserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        List<Advert> adverts = rentService.findAllObjectsByName(message);
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        if (adverts.isEmpty()) {
            sendMessage.setText(String.format(Text.NOT_FOUND_ADVERTS, message));
            user.setState(State.RENTING);
            userRepository.save(user);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(List.of(
                    List.of(
                            createButton("Давай", RentCommand.CREATE_RENT_REQUEST),
                            createButton("Не буду", RentCommand.DONT_CREATE_RENT_REQUEST))
            ));
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            messages.add(sendMessage);
        } else {
            sendMessage.setText(String.format(Text.ADVERTS_FOUND, adverts.stream()
                            .map(Advert::getFullDescription)
                            .collect(Collectors.toList())));
            messages.add(sendMessage);
            messages.addAll(MenuService.createMenu(user, Text.MAIN_MENU));
        }
        return messages;
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.ASK_ABOUT_RENTING);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
