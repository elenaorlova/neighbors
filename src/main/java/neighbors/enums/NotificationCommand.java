package neighbors.enums;

import neighbors.enums.bot.Command;

public interface NotificationCommand extends Command {
    String ENABLE_RENT_NOTIFICATIONS = "/enable_rent_notifications";
    String DISABLE_RENT_NOTIFICATIONS = "/disable_rent_notifications";
    String USER_DISTRICT_NOTIFICATIONS = "/user_district_notifications";
    String SEVERAL_DISTRICTS_NOTIFICATIONS = "/several_districts_notifications";
    String ALL_DISTRICTS_NOTIFICATIONS = "/all_districts_notifications";
}
