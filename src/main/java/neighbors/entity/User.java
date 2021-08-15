package neighbors.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neighbors.enums.State;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "BOT_USER")
@Setter
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "district")
    private String district;

    @Column(name = "state", nullable = false)
    private State state = State.NEW_USER;

    @Column(name = "current_advert")
    private Long currentAdvert;

    public User(Long chatId, String username, String name) {
        this.chatId = chatId;
        this.username = username;
        this.name = name;
    }
}
