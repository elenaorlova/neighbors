package neighbors.enums.bot;

import lombok.extern.slf4j.Slf4j;

public interface Text {

    String NOTIFICATIONS_TURN_ON_IN_USER_DISTRICT = "Договорились! Буду отправлять оповещения о съёме в твоём районе";
    String SELECTING_SEVERAL_AREAS_FOR_NOTIFICATIONS = "Введи название районов через запятую";
    String NOTIFICATIONS_TURN_ON_IN_SEVERAL_DISTRICTS = "Договорились! Буду отправлять оповещения о съеме вещей в этих районах: %s";
    String REQUEST_RENTING_NAME = "Какой товар вы хотите снять? Введите название (например, дрель)";
    String REQUEST_RENTING_OUT_NAME = "Введите название товара, который собираетесь сдавать (например, дрель)";
    String WELCOME_MESSAGE = "Привет! Я сервис шеринга для соседей. Тут ты можешь сдавать свои вещи и брать в аренду другие.";
    String SELECT_USER_DISTRICT = "В каком районе ты находишься?";
    String NOT_FOUND_ADVERTS = "К сожалению, %s пока никто не сдаёт, но ты можешь оставить запрос. Делаем?";
    String ADVERTS_FOUND = "Я нашел для тебя следующие товары: %s";
    String ENABLING_NOTIFICATIONS = "Об объявлениях в каком районе ты хочешь получать уведомления?";
    String DISABLE_NOTIFICATIONS = "Окей, не буду отправлять тебе уведомления.";
    String BUTTON_ONLY_MY_DISTRICT = "Только в своём";
    String BUTTON_SEVERAL_DISTRICTS = "Выбрать несколько";
    String BUTTON_ALL_DISTRICTS = "Во всех";
    String REQUEST_PRODUCT_PRICE = "Введите стоимость аренды товара (в рублях)";
    String REQUEST_PRODUCT_DESCRIPTION = "Введите краткое описание товара";
    String CONFIRM_ADVERT = "Отлично! Мы разместили ваше объявление о товаре. Теперь оно выглядит так:";
    String ADVERT_TEXT = "@%s сдает в аренду %s по цене %s рублей в сутки.\nРайон: %s\nОписание товара от автора:\\n%s";
    String MAIN_MENU = "Хочешь арендовать товар или зашерить свой?";
    String BUTTON_RENT = "Сдать";
    String BUTTON_RENT_OUT = "Снять";
    String DISTRICT_NOT_FOUND = "Не знаю про такой район. Может ты имел в виду какой-то из этих?\n%s";
    String REQUEST_TO_ENABLE_NOTIFICATIONS = "Хочешь ли ты получать оповещения о съеме вещей?";
    String USER_ENABLE_NOTIFICATIONS = "Да";
    String USER_DISABLE_NOTIFICATIONS = "Нет";
    String NOTIFICATIONS_TURN_ON_IN_ALL_DISTRICTS = "Договорились! Буду отправлять оповещения обо всех объявлениях о съёме";
}
