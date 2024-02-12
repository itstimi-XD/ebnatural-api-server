package ebnatural.bizcurator.apiserver.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "product_category")
@Getter
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category category = (Category) o;
        return getId() != null && Objects.equals(getId(), category.getId());
    }


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
