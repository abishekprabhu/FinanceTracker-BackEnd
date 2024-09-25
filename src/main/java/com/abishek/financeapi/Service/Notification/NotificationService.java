package com.abishek.financeapi.Service.Notification;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    public void sendRealTimeNotification(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}

