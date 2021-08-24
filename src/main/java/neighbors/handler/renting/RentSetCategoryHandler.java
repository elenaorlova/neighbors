package neighbors.handler.renting;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.entity.User;
import neighbors.enums.AdvertType;
import neighbors.enums.CategoryCommand;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.repository.UserRepository;
import neighbors.service.AdvertService;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentSetCategoryHandler implements Handler {

    private final UserRepository userRepository;
    private final AdvertService advertService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(user);
        Advert advert = advertService.saveAdvert(user, AdvertType.RENT);
        user.setCurrentAdvert(advert.getId());
        userRepository.save(user);
        advertService.setAdvertCategory(advert, message);
        sendMessage.setText(Text.REQUEST_RENTING_OUT_NAME);
        user.setState(State.RENTING_SET_NAME);
        userRepository.save(user);
        return List.of(sendMessage);
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.RENTING_SELECT_CATEGORY);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(CategoryCommand.CLOTHES, CategoryCommand.OTHER, CategoryCommand.REPAIR_THINGS);
    }
}
