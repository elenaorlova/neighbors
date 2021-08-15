package neighbors.repository;

import neighbors.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification getNotificationByUserId(Long userId);
}
