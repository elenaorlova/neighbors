package neighbors.repository;

import neighbors.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getByChatId(Long chatId);

    @Query(value = "select * from users bu" +
            " join district d on d.id = bu.user_district_id" +
            " where bu.sent_notifications = ?1 and (name = ?2 or name = 'all')", nativeQuery = true)
    List<User> findAllBySentNotificationsAndNotificationDistrictsContains(Boolean isSentNotifications, String district);
}
