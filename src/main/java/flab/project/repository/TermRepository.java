package flab.project.repository;

import flab.project.domain.Terms;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<Terms, Long> {

    Optional<Terms> findById(Long id);
}
