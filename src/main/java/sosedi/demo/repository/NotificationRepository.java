package sosedi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sosedi.demo.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification getNotificationByUserId(Long userId);
}
