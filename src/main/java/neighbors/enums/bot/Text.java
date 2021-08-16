package neighbors.enums.bot;

import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Slf4j
public enum Text {

    NOTIFICATIONS_TURN_ON_IN_USER_DISTRICT,
    SELECTING_SEVERAL_AREAS_FOR_NOTIFICATIONS,
    NOTIFICATIONS_TURN_ON_IN_SEVERAL_DISTRICTS,
    REQUEST_RENTING_NAME,
    REQUEST_RENTING_OUT_NAME,
    WELCOME_MESSAGE,
    SELECT_USER_DISTRICT,
    NOT_FOUND_ADVERTS,
    ADVERTS_FOUND,
    ENABLING_NOTIFICATIONS,
    DISABLE_NOTIFICATIONS,
    BUTTON_ONLY_MY_DISTRICT,
    BUTTON_SEVERAL_DISTRICTS,
    BUTTON_ALL_DISTRICTS,
    REQUEST_PRODUCT_PRICE,
    REQUEST_PRODUCT_DESCRIPTION,
    CONFIRM_ADVERT,
    ADVERT_TEXT,
    MAIN_MENU,
    BUTTON_RENT,
    BUTTON_RENT_OFF,
    DISTRICT_NOT_FOUND,
    REQUEST_TO_ENABLE_NOTIFICATIONS,
    USER_ENABLE_NOTIFICATIONS,
    USER_DISABLE_NOTIFICATIONS,
    NOTIFICATIONS_TURN_ON_IN_ALL_DISTRICTS;

    public String getText(Object... params) {
        try {
            String message = ResourceBundle.getBundle("text").getString(name());
            if (params != null) {
                message = MessageFormat.format(message, params);
            }
            return message;
        } catch (MissingResourceException e) {
            log.error(e.getMessage());
            return name();
        }
    }
}
