package Repositories;

import Entities.Creneau;
import Entities.Medecin;
import Entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreneauRepository extends JpaRepository<Creneau, Integer> {
    List<Creneau> findByMedecinAndJourAndEstDispoTrue(Medecin medecin, String jour);
    List<Creneau> findByPatient(Patient patient);
    List<Creneau> findByMedecinAndEstDispoFalse(Medecin medecin);
    Optional<Creneau> findByMedecinAndJourAndHeureAndEstDispoTrue(Medecin medecin, String jour, String heure);
}
