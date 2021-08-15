package neighbors.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "NOTIFICATION")
@Setter
@Getter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @OneToMany(fetch = FetchType.EAGER)
    private List<NotificationDistrict> notificationDistricts;

    public Notification(Long userId, boolean enabled) {
        this.userId = userId;
        this.enabled = enabled;
    }
}
