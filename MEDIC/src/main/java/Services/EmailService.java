package Services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    //mail qui va s'envoyer pour recevoir le code
    public void envoyerCodeOtp(String destinataire, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@medic.fr");
        message.setTo(destinataire);
        message.setSubject("Votre code de connexion MEDIC");
        message.setText(
            "Bonjour,\n\n" +
            "Votre code de connexion est : " + code + "\n\n" +
            "Ce code est valable 10 minutes.\n\n" +
            "Si vous n'êtes pas à l'origine de cette demande, ignorez ce message."
        );
        mailSender.send(message);
    }
}
