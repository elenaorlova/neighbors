package neighbors.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neighbors.enums.AdvertType;

import javax.persistence.*;

@Entity(name = "ADVERT")
@Setter
@Getter
@NoArgsConstructor
public class Advert {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "advert_type", nullable = false)
    private AdvertType advertType;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @OneToOne
    private District district;

    @Column(name = "full_description")
    private String fullDescription;

    // todo: add photos
}
