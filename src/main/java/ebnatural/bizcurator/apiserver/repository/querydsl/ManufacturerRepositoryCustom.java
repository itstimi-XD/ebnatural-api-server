package ebnatural.bizcurator.apiserver.repository.querydsl;

import ebnatural.bizcurator.apiserver.domain.Manufacturer;

public interface ManufacturerRepositoryCustom {
    Manufacturer findOrCreateManufacturer(String name);
}