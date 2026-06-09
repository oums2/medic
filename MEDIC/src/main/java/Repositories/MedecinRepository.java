package Repositories;

import Entities.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Integer> {
    List<Medecin> findBySpecialite(String specialite);
}
