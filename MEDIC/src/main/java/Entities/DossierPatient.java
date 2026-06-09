package Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "dossiers_patient")
public class DossierPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    private String traitements = "Vide";

    @Column(name = "antecedents")
    private String antecedents = "Vide";

    public DossierPatient() {}

    public DossierPatient(Patient patient) {
        this.patient = patient;
    }

    public void afficheDossier() {
        System.out.println("Nom : " + patient.getNom() + "  Prénom : " + patient.getPrenom());
        System.out.println("Email : " + patient.getEmail());
        System.out.println("Numéro : " + patient.getNumero());
        System.out.println("Adresse : " + patient.getAdresse());
        System.out.println("Traitements en cours : " + this.traitements);
        System.out.println("Antécédents : " + this.antecedents);
        System.out.println("Rendez-vous prévus :");
        patient.afficheMesRdv();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getTraitements() { return traitements; }
    public void setTraitements(String traitements) { this.traitements = traitements; }

    public String getAntecedents() { return antecedents; }
    public void setAntecedents(String antecedents) { this.antecedents = antecedents; }
}
