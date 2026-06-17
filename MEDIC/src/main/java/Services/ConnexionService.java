package Services;

import Entities.Utilisateur;
import Repositories.UtilisateurRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConnexionService {

    private static final java.util.regex.Pattern EMAIL_PATTERN =
        java.util.regex.Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final UtilisateurRepository utilisateurRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    ConnexionService(UtilisateurRepository utilisateurRepo, BCryptPasswordEncoder passwordEncoder){
        this.utilisateurRepo = utilisateurRepo;
        this.passwordEncoder = passwordEncoder;
    }

    private void verifierFormatEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Adresse email invalide.");
    }

    public Utilisateur seConnecter(String email, String motDePasse){
        verifierFormatEmail(email);
        Utilisateur utilisateur = utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect."));
        if (!passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect.");
        return utilisateur;
    }
}
