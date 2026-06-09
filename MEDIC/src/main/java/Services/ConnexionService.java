package Services;

import Entities.Utilisateur;
import Repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnexionService {

    @Autowired private UtilisateurRepository utilisateurRepo;

    public Utilisateur seConnecter(String email, String motDePasse) {
        return utilisateurRepo.findByEmailAndMotDePasse(email, motDePasse)
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect."));
    }
}
