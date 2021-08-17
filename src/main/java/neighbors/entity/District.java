package neighbors.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "DISTRICT")
@Setter
@Getter
@NoArgsConstructor
public class District {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    public District(String name) {
        this.name = name;
    }
}
