package neighbors.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "NOTIFICATION_DISTRICT")
@Setter
@Getter
@NoArgsConstructor
public class District {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "district_name")
    private String districtName;

    public District(String districtName) {
        this.districtName = districtName;
    }
}
