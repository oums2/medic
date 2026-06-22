package Repositories;

import Entities.Patient;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>{
    List<Patient> findByNomContainingIgnoreCase(String nom);
    // Recherche dans nom, prénom 
    List<Patient> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
}
