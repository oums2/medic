package Utils;

import Entities.Creneau;
import Entities.DossierPatient;
import Entities.Medecin;
import Entities.Patient;

public class Affichage {

    public static void affichePlanning(Medecin medecin) {
        System.out.println("Planning de Dr." + medecin.getNom() + " " + medecin.getPrenom());
        boolean aucunRdv = true;
        if (medecin.getCreneaux() != null) {
            for (Creneau c : medecin.getCreneaux()) {
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

    public static void afficherDossierPatient(DossierPatient dossier) {
        Patient patient = dossier.getPatient();
        System.out.println("Nom : " + patient.getNom() + "  Prénom : " + patient.getPrenom());
        System.out.println("Email : " + patient.getEmail());
        System.out.println("Numéro : " + patient.getNumero());
        System.out.println("Adresse : " + patient.getAdresse());
        System.out.println("Traitements en cours : " + dossier.getTraitements());
        System.out.println("Antécédents : " + dossier.getAntecedents());
        System.out.println("Rendez-vous prévus :");
        afficheMesRdv(patient);
    }

    public static void afficheMesRdv(Patient patient) {
        System.out.println("Mes rendez-vous :");
        if (patient.getRdv() == null || patient.getRdv().isEmpty()) {
            System.out.println("  Aucun rendez-vous prévu.");
            return;
        }
        for (Creneau c : patient.getRdv()) {
            System.out.println("  " + c.getJour() + " à " + c.getHeure()
                    + " avec Dr. " + c.getMedecin().getNom() + " " + c.getMedecin().getPrenom());
        }
        System.out.println();
    }
}
