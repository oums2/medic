package Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "medecins") // modification dans la table medecins
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Medecin extends Utilisateur {

    private String specialite;

    @JsonIgnore //ignore pour éviter les erreurs
    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // un medecin pour plusieurs créneaux
    private List<Creneau> creneaux;

    public Medecin(){}

    public Medecin(String nom, String prenom, String email, String specialite, String motDePasse){
        super(nom, prenom, email, motDePasse);
        this.specialite = specialite;
    }

    public String getSpecialite(){ 
        return specialite;
    }
    public void setSpecialite(String specialite){ 
        this.specialite = specialite; 
    }
    
    public List<Creneau> getCreneaux(){ 
        return creneaux; 
    }
    public void setCreneaux(List<Creneau> creneaux){ 
        this.creneaux = creneaux; 
    }
}
