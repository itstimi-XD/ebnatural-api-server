package ebnatural.bizcurator.apiserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 제작목적
 * 1. 창업(제품판매)
 * 2. 작품제작
 * 3. 개인적인 목적
 */
@Entity
@Getter
public class PurposeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(nullable = false)
    private String name;
}
