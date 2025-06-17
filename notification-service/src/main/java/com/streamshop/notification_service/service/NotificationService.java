package com.streamshop.notification_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.streamshop.notification_service.events.OrderPlacedEvents;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "order-placed")
    public void handleOrderPlaced(OrderPlacedEvents event) {

        sendOrderConfirmationEmail(event);
    }

    private void sendOrderConfirmationEmail(OrderPlacedEvents event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());

        message.setFrom("streamshop@gmail.com");

        message.setSubject("Order Confirmation - StreamShop");
        message.setText("Dear Customer,\n\n" +
                "Thank you for your order! We're pleased to confirm that your order with ID " + event.getOrderId() +
                " has been received and is being processed.\n\n" +
                "You will receive another notification when your order ships.\n\n" +
                "If you have any questions about your order, please contact our customer service.\n\n" +
                "Thank you for shopping with us!\n\n" +
                "Best regards,\n" +
                "The StreamShop Team");

        mailSender.send(message);
    }
}