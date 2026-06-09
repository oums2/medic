package Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "medecins")
public class Medecin extends Utilisateur {

    private String specialite;

    @JsonIgnore
    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Creneau> creneaux;

    public Medecin() {}

    public Medecin(String nom, String prenom, String email, String specialite, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.specialite = specialite;
    }

    public void affichePlanning() {
        System.out.println("Planning de Dr." + this.getNom() + " " + this.getPrenom());
        boolean aucunRdv = true;
        if (creneaux != null) {
            for (Creneau c : creneaux) {
                if (!c.isEstDispo()) {
                    System.out.println("  " + c.getJour() + " à " + c.getHeure()
                            + " avec " + c.getPatient().getNom() + " " + c.getPatient().getPrenom());
                    aucunRdv = false;
                }
            }
        }
        if (aucunRdv) {
            System.out.println("  Aucun RDV prévu.");
        }
        System.out.println();
    }

    public void afficherDossierPatient(Patient p) {
        p.getDossier().afficheDossier();
    }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public List<Creneau> getCreneaux() { return creneaux; }
    public void setCreneaux(List<Creneau> creneaux) { this.creneaux = creneaux; }
}
