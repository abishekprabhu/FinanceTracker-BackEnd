package com.abishek.financeapi.Controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.abishek.financeapi.DTO.NotificationDTO;

@RestController
@CrossOrigin("*")
public class NotificationController {

    // Handle messages sent to /app/sendNotification
	
	 private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendIncomeNotification(double amount, double balance) {
        NotificationDTO notification = new NotificationDTO("credit", "Income added", amount ,balance);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    public void sendExpenseNotification(double amount , double balance) {
        NotificationDTO notification = new NotificationDTO("debit", "Expense added", amount, balance);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
    
    public void sendWalletNotification(double amount , double balance) {
        NotificationDTO notification = new NotificationDTO("credit", "Wallet added", amount, balance);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
    
    public void sendBillNotification(double amount , double balance) {
        NotificationDTO notification = new NotificationDTO("credit", "Bill Payed", amount, balance);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
    
    public void sendNotifications(String message) {
        // This message will be broadcast to all subscribers of /topic/notifications
    	 messagingTemplate.convertAndSend("/topic/notifications", message);
    }
    
//    @MessageMapping("/markAsRead")
//    public void markAsRead(NotificationIdRequest request) {
//        // Call service to mark the notification as read in the database
//        notificationService.markAsRead(request.getId());
//    }
    
    
    @MessageMapping("/sendNotification")
    @SendTo("/topic/notifications")
    public String sendNotification(String message) {
        // This message will be broadcast to all subscribers of /topic/notifications
        return message;
    }
    
}


