package neighbors.enums;

import neighbors.enums.bot.Command;

public interface RentCommand extends Command {
    String CREATE_RENT_REQUEST = "/create_rent_request";
    String DONT_CREATE_RENT_REQUEST = "/dont_create_rent_request";
}
