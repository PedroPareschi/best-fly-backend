package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.entity.PriceAlertSubscription;
import com.pedropareschi.bestfly.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailService(JavaMailSender mailSender, @Value("${alerts.mail.from:no-reply@bestfly.local}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    public void sendPriceAlert(User user, PriceAlertSubscription alert, BigDecimal amount, String currency, String carrierName) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }

        String subject = buildSubject(alert, amount, currency, carrierName);
        String body = buildBody(user, alert, amount, currency, carrierName);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(fromAddress);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    private static String buildSubject(PriceAlertSubscription alert, BigDecimal amount, String currency, String carrierName) {
        String route = alert.getOriginLocation() + " -> " + alert.getDestinationLocation();
        String price = amount.toPlainString() + " " + currency;
        if (carrierName != null && !carrierName.isBlank()) {
            return "Alerta de menor preco: " + route + " por " + price + " (" + carrierName + ")";
        }
        return "Alerta de menor preco: " + route + " por " + price;
    }

    private static String buildBody(User user, PriceAlertSubscription alert, BigDecimal amount, String currency, String carrierName) {
        StringBuilder builder = new StringBuilder();
        builder.append("Ola ").append(user.getFirstName() != null ? user.getFirstName() : "viajante").append(",\n\n");
        builder.append("Encontramos um menor preco para o seu alerta:\n");
        builder.append("Rota: ").append(alert.getOriginLocation()).append(" -> ").append(alert.getDestinationLocation()).append("\n");
        if (alert.getDepartureDate() != null) {
            builder.append("Ida: ").append(alert.getDepartureDate());
            if (alert.getDepartureTime() != null && !alert.getDepartureTime().isBlank()) {
                builder.append(" ").append(alert.getDepartureTime());
            }
            builder.append("\n");
        }
        if (alert.getReturnDate() != null) {
            builder.append("Volta: ").append(alert.getReturnDate());
            if (alert.getReturnTime() != null && !alert.getReturnTime().isBlank()) {
                builder.append(" ").append(alert.getReturnTime());
            }
            builder.append("\n");
        }
        builder.append("Passageiros: ").append(alert.getNumberOfAdults()).append(" adulto(s)");
        if (alert.getNumberOfChildren() > 0) {
            builder.append(" + ").append(alert.getNumberOfChildren()).append(" crianca(s)");
        }
        builder.append("\n");
        builder.append("Preco: ").append(amount.toPlainString()).append(" ").append(currency).append("\n");
        if (carrierName != null && !carrierName.isBlank()) {
            builder.append("Companhia: ").append(carrierName).append("\n");
        }
        builder.append("\nAcesse o app para ver os detalhes e reservar.\n");
        return builder.toString();
    }
}
