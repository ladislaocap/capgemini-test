package com.capgemini.test.code.infrastructure.notification;

import com.capgemini.test.code.application.exception.ExternalServiceException;
import com.capgemini.test.code.application.port.out.EmailNotifier;
import com.capgemini.test.code.infrastructure.client.NotificationClient;
import com.capgemini.test.code.infrastructure.client.dto.EmailNotificationRequest;
import feign.FeignException;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationAdapter implements EmailNotifier {
  private final NotificationClient notificationClient;

  public EmailNotificationAdapter(NotificationClient notificationClient) {
    this.notificationClient = notificationClient;
  }

  @Override
  public void send(String email, String message) {
    try {
      notificationClient.sendEmail(new EmailNotificationRequest(email, message));
    } catch (FeignException exception) {
      throw new ExternalServiceException("Email notification", exception);
    }
  }
}
