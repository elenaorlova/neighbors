package neighbors.repository;

import neighbors.entity.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BotUserRepository extends JpaRepository<BotUser, Long> {
    Optional<BotUser> getByChatId(Long chatId);

    @Query(value = "select * from bot_user bu" +
            " join district d on d.id = bu.user_district_id" +
            " where bu.sent_notifications = ?1 and (name = ?2 or name = 'all')", nativeQuery = true)
    List<BotUser> findAllBySentNotificationsAndNotificationDistrictsContains(Boolean isSentNotifications, String district);
}
