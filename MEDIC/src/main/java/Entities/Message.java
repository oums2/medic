package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expediteur_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "motDePasse"})
    private Utilisateur expediteur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destinataire_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "motDePasse"})
    private Utilisateur destinataire;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime date = LocalDateTime.now();

    private boolean lu = false;

    public Message() {}

    public Message(Utilisateur expediteur, Utilisateur destinataire, String contenu) {
        this.expediteur   = expediteur;
        this.destinataire = destinataire;
        this.contenu      = contenu;
    }

    public int getId() { return id; }
    public Utilisateur getExpediteur() { return expediteur; }
    public Utilisateur getDestinataire() { return destinataire; }
    public String getContenu() { return contenu; }
    public LocalDateTime getDate() { return date; }
    public boolean isLu() { return lu; }
    public void setLu(boolean lu) { this.lu = lu; }
}
