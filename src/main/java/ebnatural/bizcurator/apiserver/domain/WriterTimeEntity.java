package ebnatural.bizcurator.apiserver.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class WriterTimeEntity extends TimeEntity {

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 100)
    protected String createdBy;

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    protected String modifiedBy;
}
