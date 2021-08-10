package sosedi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sosedi.demo.entity.Objects;

import java.util.List;

@Repository
public interface ObjectsRepository extends JpaRepository<Objects, Long> {
    List<Objects> findAllByNameContains(String name);
}
