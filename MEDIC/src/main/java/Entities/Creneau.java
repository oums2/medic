package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "creneaux") // modification dans la table creneaux
public class Creneau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id se génére automatiquement
    private int id;

    private String jour;
    private String heure;

    @Column(name = "est_dispo") // modification dans la colonne est_dispo
    private boolean estDispo = true;

    @ManyToOne(fetch = FetchType.LAZY) // peux y avoir plusieurs médecins
    @JoinColumn(name = "medecin_id", nullable = false) // modification dans la colonne medecin_id (clé étrangère)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // ignore les champs car crée des erreurs
    private Medecin medecin;

    @ManyToOne(fetch = FetchType.LAZY) // peux y avoir plusieurs patients
    @JoinColumn(name = "patient_id") // modification dans la colonne patient_id (clé étrangère)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // ignore les champs car crée des erreurs
    private Patient patient;

    public Creneau(){}

    public Creneau(String jour, String heure, Medecin medecin){
        this.jour = jour;
        this.heure = heure;
        this.medecin = medecin;
        this.estDispo = true;
    }

    public int getId(){
        return id; 
    }
    public void setId(int id){ 
        this.id = id; 
    }

    public String getJour(){ 
        return jour; 
    }
    public void setJour(String jour){ 
        this.jour = jour; 
    }

    public String getHeure(){ 
        return heure; 
    }
    public void setHeure(String heure){ 
        this.heure = heure; 
    }

    public boolean isEstDispo(){ 
        return estDispo; 
    }
    public void setEstDispo(boolean estDispo){ 
        this.estDispo = estDispo; 
    }

    public Medecin getMedecin(){ 
        return medecin; 
    }
    public void setMedecin(Medecin medecin){ 
        this.medecin = medecin; 
    }

    public Patient getPatient(){ 
        return patient; 
    }
    public void setPatient(Patient patient){ 
        this.patient = patient; 
    }
}
