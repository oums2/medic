package Controllers;

import Entities.Utilisateur;
import Services.ConnexionService;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ConnexionController {

    private final ConnexionService connexionService;

    ConnexionController(ConnexionService connexionService) {
        this.connexionService = connexionService;
    }

    // Étape 1 : vérifie email + mot de passe, envoie le code par mail
    @PostMapping("/connexion")
    public Map<String, String> connexion(@RequestBody Map<String, String> body) {
        return connexionService.seConnecter(body.get("email"), body.get("motDePasse"));
    }

    // Étape 2 : vérifie le code OTP et retourne l'utilisateur complet
    @PostMapping("/connexion/verifier-code")
    public Utilisateur verifierCode(@RequestBody Map<String, String> body) {
        return connexionService.verifierCode(body.get("email"), body.get("code"));
    }
}
