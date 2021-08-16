package neighbors.repository;

import neighbors.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationDistrictRepository extends JpaRepository<District, Long> {
}
