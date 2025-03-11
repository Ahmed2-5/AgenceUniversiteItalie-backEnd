package Agence.AgenceUniversiteItalie_backEnd.service;


import Agence.AgenceUniversiteItalie_backEnd.entity.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void envoyerEmailActivation(Utilisateur utilisateur) {
        try {
            if(utilisateur.getAdresseMail() == null || utilisateur.getAdresseMail().isEmpty()) {
                System.out.println("Erreur : adresse mail non valide");
                return;
            }

            String sujet = "Activation de votre compte Universite Italie";
            String lienActivation = "http://localhost:8082/api/utilisateurs/activer-compte?email=" + utilisateur.getAdresseMail();

            String message = "Bonjour !" + utilisateur.getNom() + ",\n\n"
                    + "Merci pour votre Inscription sur Universite Italie! \n"
                    + "Veuillez cliquer sur le lien pour activer votre Compte: \n\n"
                    +lienActivation + "\n\n"
                    +"Cordialement, \n L'equipe Université Italie.";

            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("Noreply@universiteitalie.com");
            email.setTo(utilisateur.getAdresseMail());
            email.setSubject(sujet);
            email.setText(message);

            mailSender.send(email);
            System.out.println("email d'activation a été envoyer a : " + utilisateur.getAdresseMail());
        }catch (Exception e){
            System.err.println("erreur lors de l'envoi d'un email : " + e.getMessage());
        }
    }

    public void sendSimpleEmail(String client , String subject , String message){
        try {
            if (client == null || client.isEmpty()){
                System.out.println("l'adresse est vide");
                return;
            }

            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("Noreply@universiteitalie.com");
            email.setTo(client);
            email.setSubject(subject);
            email.setText(message);

            mailSender.send(email);
            System.out.println("email envoyer a :" +client);
        }catch (Exception e){
            System.err.println("Errueur lors de l'envoi de l'email:" +e.getMessage());
        }
    }
}
