package neighbors.enums;

import neighbors.enums.bot.Command;

public interface MainCommand extends Command {
    String RENT = "/rent";
    String RENT_OUT = "/rent_out";
    String GET_MY_ADVERTS = "/get_my_adverts";
    String GET_RENT_OUT_ADVERTS = "/get_rent_out_adverts";
    String GET_RENT_ADVERTS = "/get_rent_adverts";
}
