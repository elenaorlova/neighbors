package neighbors.handler.notification;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.entity.BotUser;
import neighbors.enums.bot.State;
import neighbors.enums.bot.Text;
import neighbors.handler.Handler;
import neighbors.repository.AdvertRepository;
import neighbors.repository.BotUserRepository;
import neighbors.service.MainService;
import neighbors.service.NotificationService;
import neighbors.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentingOutSetDescriptionHandler implements Handler {

    private final AdvertRepository advertRepository;
    private final BotUserRepository botUserRepository;
    private final NotificationService notificationService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotUser botUser, String message) {
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        SendMessage sendMessage = TelegramUtils.createMessageTemplate(botUser);
        Advert advert = advertRepository.findAdvertByChatIdAndId(botUser.getChatId(), botUser.getCurrentAdvert());
        advert.setDescription(message);
        String advertText = buildAdvertMessage(botUser, advert);
        advert.setFullDescription(advertText);
        advertRepository.save(advert);
        botUser.setState(State.REGISTERED);
        sendMessage.setText(Text.CONFIRM_ADVERT.getText());
        SendMessage advertMessage = TelegramUtils.createMessageTemplate(botUser);
        advertMessage.setText(advertText);
        messages.add(sendMessage);
        messages.add(advertMessage);
        messages.addAll(MainService.createMainMenu(botUser));
        messages.addAll(notificationService.sendNotification(advert, botUser));
        return messages;
    }

    private String buildAdvertMessage(BotUser botUser, Advert advert) {
        return Text.ADVERT_TEXT.getText(
                botUser.getUsername(),
                advert.getName(),
                advert.getPrice(),
                botUser.getUserDistrict().getName(),
                advert.getDescription()
        );
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.RENTING_OUT_SET_DESCRIPTION);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
