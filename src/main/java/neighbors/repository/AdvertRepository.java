package neighbors.repository;

import neighbors.entity.Advert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {
    List<Advert> findAllByNameContains(String name);
    Advert findAdvertByChatIdAndId(Long chatId, Long id);
}
