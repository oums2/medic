package Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "patients") // modification dans la table patients
public class Patient extends Utilisateur {

    private String adresse;
    private String numero;

    @JsonIgnore //ignore pour éviter les erreurs
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // un dossier par patient
    private DossierPatient dossier;

    @JsonIgnore //ignore pour éviter les erreurs
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY) // un patient pour plusieurs créneau
    private List<Creneau> rdv;

    public Patient() {}

    public Patient(String nom, String prenom, String email, String adresse, String numero, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.adresse = adresse;
        this.numero = numero;
    }

    public String getAdresse(){ 
        return adresse; 
    }
    public void setAdresse(String adresse){ 
        this.adresse = adresse; 
    }

    public String getNumero(){ 
        return numero; 
    }
    public void setNumero(String numero){ 
        this.numero = numero; 
    }

    public DossierPatient getDossier(){ 
        return dossier; 
    }
    public void setDossier(DossierPatient dossier){ 
        this.dossier = dossier;
    }

    public List<Creneau> getRdv(){ 
        return rdv; 
    }
    public void setRdv(List<Creneau> rdv){ 
        this.rdv = rdv; 
    }
}
