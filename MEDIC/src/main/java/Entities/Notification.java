package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "motDePasse"})
    private Utilisateur utilisateur;

    private String message;

    private boolean lue = false;

    private LocalDateTime date = LocalDateTime.now();

    public Notification() {}

    public Notification(Utilisateur utilisateur, String message) {
        this.utilisateur = utilisateur;
        this.message = message;
    }

    public int getId() { return id; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public String getMessage() { return message; }
    public boolean isLue() { return lue; }
    public void setLue(boolean lue) { this.lue = lue; }
    public LocalDateTime getDate() { return date; }
}
