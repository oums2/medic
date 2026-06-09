package Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient extends Utilisateur {

    private String adresse;
    private String numero;

    @JsonIgnore
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DossierPatient dossier;

    @JsonIgnore
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Creneau> rdv;

    public Patient() {}

    public Patient(String nom, String prenom, String email, String adresse, String numero, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.adresse = adresse;
        this.numero = numero;
    }

    public void afficheMesRdv() {
        System.out.println("Mes rendez-vous :");
        if (rdv == null || rdv.isEmpty()) {
            System.out.println("  Aucun rendez-vous prévu.");
            return;
        }
        for (Creneau c : rdv) {
            System.out.println("  " + c.getJour() + " à " + c.getHeure()
                    + " avec Dr. " + c.getMedecin().getNom() + " " + c.getMedecin().getPrenom());
        }
        System.out.println();
    }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public DossierPatient getDossier() { return dossier; }
    public void setDossier(DossierPatient dossier) { this.dossier = dossier; }

    public List<Creneau> getRdv() { return rdv; }
    public void setRdv(List<Creneau> rdv) { this.rdv = rdv; }
}
