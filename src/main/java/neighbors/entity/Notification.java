package neighbors.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "NOTIFICATION")
@Setter
@Getter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToOne
    private Advert advert;

}
