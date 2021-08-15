package neighbors.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "NOTIFICATION_DISTRICT")
@Setter
@Getter
@NoArgsConstructor
public class NotificationDistrict {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "district")
    private String district;

    public NotificationDistrict(String district) {
        this.district = district;
    }
}
