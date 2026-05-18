package com.aibert.dosw.infrastructure.external.email;

import com.aibert.dosw.domain.ports.out.EmailServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmtpEmailService implements EmailServicePort {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(String toEmail, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Verifica tu cuenta en EciPlanner");
        message.setText("Tu código de verificación es:\n\n" + verificationLink + "\n\nEste código caduca en 5 minutos y solo puede usarse una vez.");
        mailSender.send(message);
    }

    @Override
    public void sendRecoveryEmail(String toEmail, String recoveryLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Recuperación de contraseña - EciPlanner");
        message.setText("Haz clic en el siguiente enlace para restablecer tu contraseña (caduca en 5 minutos):\n\n" + recoveryLink + "\n\nSi no solicitaste esto, ignora este correo.");
        mailSender.send(message);
    }
}
