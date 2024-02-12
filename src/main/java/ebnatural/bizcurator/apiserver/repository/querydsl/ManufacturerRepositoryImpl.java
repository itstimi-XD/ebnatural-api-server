package ebnatural.bizcurator.apiserver.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ebnatural.bizcurator.apiserver.domain.Manufacturer;
import ebnatural.bizcurator.apiserver.domain.QManufacturer;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public class ManufacturerRepositoryImpl implements ManufacturerRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    // Entity Manager를 통해 JPAQueryFactory 생성
    public ManufacturerRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Transactional
    @Override
    public Manufacturer findOrCreateManufacturer(String name) {
        QManufacturer manufacturerEntity = QManufacturer.manufacturer;

        Manufacturer manufacturer = queryFactory
                .selectFrom(manufacturerEntity)
                .where(manufacturerEntity.name.eq(name))
                .fetchOne();

        if (manufacturer == null) {
            manufacturer = new Manufacturer();
            manufacturer.setName(name);
            em.persist(manufacturer);
        }

        return manufacturer;
    }
}
