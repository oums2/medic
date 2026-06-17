package Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "dossiers_patient") // modification dans la table dossiers_patient
public class DossierPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id se génére automatiquement
    private int id;

    @OneToOne(fetch = FetchType.LAZY) // un patient à un seul dossier 
    @JoinColumn(name = "patient_id", nullable = false, unique = true) // modification dans la colonne patient_id (clé étrangère)
    private Patient patient;

    private String traitements = "Vide";

    @Column(name = "antecedents") // modification dans la colonne antecedent
    private String antecedents = "Vide";

    public DossierPatient() {}

    public DossierPatient(Patient patient){
        this.patient = patient;
    }

    public int getId(){
        return id; 
    }
    public void setId(int id){ 
        this.id = id; 
    }

    public Patient getPatient(){ 
        return patient; 
    }
    public void setPatient(Patient patient){ 
        this.patient = patient; 
    }

    public String getTraitements(){ 
        return traitements; 
    }
    public void setTraitements(String traitements){ 
        this.traitements = traitements; 
    }

    public String getAntecedents(){ 
        return antecedents; 
    }
    public void setAntecedents(String antecedents){ 
        this.antecedents = antecedents; 
    }
}
