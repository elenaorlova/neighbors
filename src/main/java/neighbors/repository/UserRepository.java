package neighbors.repository;

import neighbors.entity.User;
import neighbors.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getByChatId(Long chatId);
    List<User> findAllByState(State state);
}