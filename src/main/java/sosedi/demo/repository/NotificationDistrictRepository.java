package sosedi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sosedi.demo.entity.NotificationDistrict;

public interface NotificationDistrictRepository extends JpaRepository<NotificationDistrict, Long> {
}
