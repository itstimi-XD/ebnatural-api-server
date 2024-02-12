package ebnatural.bizcurator.apiserver.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "manufacturer")
@Getter
@ToString
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "name", nullable = false)
    @NotBlank(message = "제조사 이름은 필수 입력값입니다.")
    @Size(max = 50, message = "제조사 이름은 최대 50자까지 입력 가능합니다.")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Manufacturer that = (Manufacturer) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
