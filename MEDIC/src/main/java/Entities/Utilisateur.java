package Entities;
 
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
 
@Entity
@Table(name = "utilisateurs") //modification dans la table utilisateurs
@Inheritance(strategy = InheritanceType.JOINED) // patient et medecin seront des tables différentes
public class Utilisateur {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id se génére automatiquement
    private int id;
 
    private String nom;
    private String prenom;
    @Column(unique = true)
    private String email;
 
    @JsonIgnore //ignore pour éviter les erreurs
    @Column(name = "mot_de_passe") // modification dans la colonne mot_de_passe
    private String motDePasse;
 
    @Column(name = "is_admin") // modification dans la colonne is_admin
    private boolean isAdmin;
 
    public Utilisateur() {}
 
    public Utilisateur(String nom, String prenom, String email, String motDePasse){
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.isAdmin = false;
    }
 
    public Utilisateur(String nom, String prenom, String email, boolean isAdmin, String motDePasse){
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.isAdmin = isAdmin;
        this.motDePasse = motDePasse;
    }
 
    public int getId(){ 
        return id; 
    }
    public void setId(int id){ 
        this.id = id; 
    }
 
    public String getNom(){ 
        return nom; 
    }
    public void setNom(String nom){ 
        this.nom = nom; 
    }
 
    public String getPrenom(){ 
        return prenom; 
    }
    public void setPrenom(String prenom){ 
        this.prenom = prenom; 
    }
 
    public String getEmail(){ 
        return email; 
    }
    public void setEmail(String email){ 
        this.email = email; 
    }
 
    public String getMotDePasse(){ 
        return motDePasse; 
    }
    public void setMotDePasse(String motDePasse){ 
        this.motDePasse = motDePasse; 
    }
 
    public boolean isAdmin(){ 
        return isAdmin; 
    }
    public void setAdmin(boolean isAdmin){ 
        this.isAdmin = isAdmin; 
    }
}