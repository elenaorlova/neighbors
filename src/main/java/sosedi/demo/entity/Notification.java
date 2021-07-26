package sosedi.demo.entity;

import lombok.Getter;
import lombok.Setter;
import sosedi.demo.data.NotificationType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class Notification {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private NotificationType type;
    private Boolean enabled;
}
