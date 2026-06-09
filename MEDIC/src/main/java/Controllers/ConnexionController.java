package Controllers;

import Entities.Utilisateur;
import Services.ConnexionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class ConnexionController {

    @Autowired private ConnexionService connexionService;

    @PostMapping("/connexion")
    public Utilisateur connexion(@RequestBody Map<String, String> body) {
        return connexionService.seConnecter(body.get("email"), body.get("motDePasse"));
    }
}
