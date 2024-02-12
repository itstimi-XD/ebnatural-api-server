package ebnatural.bizcurator.apiserver.repository;


import ebnatural.bizcurator.apiserver.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);
}
