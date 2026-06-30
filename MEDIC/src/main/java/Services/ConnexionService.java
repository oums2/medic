package Services;

import Entities.Utilisateur;
import Repositories.UtilisateurRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Service
public class ConnexionService {

    private static final java.util.regex.Pattern EMAIL_PATTERN =
        java.util.regex.Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final UtilisateurRepository utilisateurRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    ConnexionService(UtilisateurRepository utilisateurRepo,
                     BCryptPasswordEncoder passwordEncoder,
                     EmailService emailService) {
        this.utilisateurRepo = utilisateurRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService    = emailService;
    }

    private void verifierFormatEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Adresse email invalide.");
    }

    // Étape 1 : vérifie email + mot de passe, génère et envoie le code OTP
    public Map<String, String> seConnecter(String email, String motDePasse) {
        verifierFormatEmail(email);
        Utilisateur utilisateur = utilisateurRepo.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect."));
        if (!passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect.");

        // Générer un code à 6 chiffres
        String code = String.format("%06d", new Random().nextInt(999999));
        utilisateur.setCodeOtp(code);
        utilisateur.setCodeExpire(LocalDateTime.now().plusMinutes(10));
        utilisateurRepo.save(utilisateur);

        emailService.envoyerCodeOtp(email, code);

        return Map.of("etape", "code_requis");
    }

    // Étape 2 : vérifie le code OTP et retourne l'utilisateur complet
    public Utilisateur verifierCode(String email, String code) {
        verifierFormatEmail(email);
        Utilisateur utilisateur = utilisateurRepo.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur introuvable."));

        if (utilisateur.getCodeOtp() == null || !utilisateur.getCodeOtp().equals(code))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Code incorrect.");

        if (utilisateur.getCodeExpire() == null || LocalDateTime.now().isAfter(utilisateur.getCodeExpire()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Code expiré. Veuillez vous reconnecter.");

        // Effacer le code après utilisation
        utilisateur.setCodeOtp(null);
        utilisateur.setCodeExpire(null);
        utilisateurRepo.save(utilisateur);

        return utilisateur;
    }
}
