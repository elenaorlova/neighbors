package neighbors.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neighbors.enums.bot.State;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name = "USER")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"chatId"})
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "username")
    private String username;

    @OneToOne
    private District userDistrict;

    @Column(name = "state", nullable = false)
    private State state = State.NEW_USER;

    @Column(name = "current_advert")
    private Long currentAdvert;

    @Column(name = "sent_notifications")
    private Boolean sentNotifications = false;

    @OneToMany(fetch = FetchType.EAGER)
    private List<District> notificationDistricts;

    public User(Long chatId, String username) {
        this.chatId = chatId;
        this.username = username;
    }
}
