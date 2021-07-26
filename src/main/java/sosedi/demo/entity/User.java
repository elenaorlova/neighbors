package sosedi.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String district;
}
