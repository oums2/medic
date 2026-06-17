package Controllers;

import Entities.Utilisateur;
import Services.ConnexionService;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // cette classe est un controller web
public class ConnexionController {

    private final ConnexionService connexionService;

    ConnexionController(ConnexionService connexionService){
        this.connexionService = connexionService;
    }

    @PostMapping("/connexion") // Utilisation de la fonction avec POST
    public Utilisateur connexion(@RequestBody Map<String, String> body){
        return connexionService.seConnecter(body.get("email"), body.get("motDePasse"));
    }
}