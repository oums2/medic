package Repositories;

import Entities.Medecin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedecinRepository extends JpaRepository<Medecin, Integer>{

    // Recherche dans nom, prénom ou spécialité (Spring génère la requête automatiquement)
    List<Medecin> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrSpecialiteContainingIgnoreCase(
        String nom, String prenom, String specialite);
}
