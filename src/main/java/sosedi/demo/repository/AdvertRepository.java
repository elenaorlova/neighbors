package sosedi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sosedi.demo.entity.Advert;

import java.util.List;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {
    List<Advert> findAllByNameContains(String name);
    Advert findAdvertByUserIdAndId(Long userId, Long id);
}
