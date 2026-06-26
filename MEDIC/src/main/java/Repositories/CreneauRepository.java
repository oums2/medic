package Repositories;

import Entities.Creneau;
import Entities.Medecin;
import Entities.Patient;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository
public interface CreneauRepository extends JpaRepository<Creneau, Integer>{
    List<Creneau> findByMedecinOrderByJourAscHeureAsc(Medecin medecin);
    List<Creneau> findByMedecinAndJourAndEstDispoTrue(Medecin medecin, String jour);
    List<Creneau> findByPatient(Patient patient);
    List<Creneau> findByMedecinAndEstDispoFalseAndValideTrue(Medecin medecin);
    List<Creneau> findByMedecinAndEstDispoFalseAndValideFalse(Medecin medecin);
    Optional<Creneau> findByMedecinAndJourAndHeureAndEstDispoTrue(Medecin medecin, String jour, String heure);
    List<Creneau> findByMedecinAndEstDispoTrue(Medecin medecin);
    boolean existsByMedecinAndJourAndHeure(Medecin medecin, String jour, String heure);
}
