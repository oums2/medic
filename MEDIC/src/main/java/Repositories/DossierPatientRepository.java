package Repositories;

import Entities.DossierPatient;
import Entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DossierPatientRepository extends JpaRepository<DossierPatient, Integer> {
    Optional<DossierPatient> findByPatient(Patient patient);
}
