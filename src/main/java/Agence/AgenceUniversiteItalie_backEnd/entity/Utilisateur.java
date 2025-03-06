package Agence.AgenceUniversiteItalie_backEnd.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUtilisateur;

    private String nom;
    private String prenom;

    @JsonProperty("adresseMail")
    private String adresseMail;

    @JsonProperty("motDePasse")
    private String motDePasse;

    private String telephone;

    private LocalDate dateDeNaissance;

    private Long idTypeAuthentification;

    private String idFacebook;

    private String idGoogle;

    private LocalDateTime dateCreation;

    private LocalDateTime dateDerniereConnexion;



    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "id_status-compte")
    private StatusCompte statusCompte;


    // ajout d'un constructeur
    public Utilisateur(String nom, String prenom, String adresseMail, String motDePasse, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresseMail = adresseMail;
        this.motDePasse = motDePasse;
        this.role = role;
        this.dateCreation = LocalDateTime.now();
        this.dateDerniereConnexion =LocalDateTime.now();
    }

    public String getMotDePasse() {return motDePasse;}

    @PrePersist
    protected void onCreate() {this.dateCreation = LocalDateTime.now();} //Définition automatique de la date


}
