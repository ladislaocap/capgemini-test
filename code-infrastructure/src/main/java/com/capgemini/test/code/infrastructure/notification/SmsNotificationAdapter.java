package com.capgemini.test.code.infrastructure.notification;

import com.capgemini.test.code.application.exception.ExternalServiceException;
import com.capgemini.test.code.application.port.out.SmsNotifier;
import com.capgemini.test.code.infrastructure.client.NotificationClient;
import com.capgemini.test.code.infrastructure.client.dto.SmsNotificationRequest;
import feign.FeignException;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationAdapter implements SmsNotifier {
  private final NotificationClient notificationClient;

  public SmsNotificationAdapter(NotificationClient notificationClient) {
    this.notificationClient = notificationClient;
  }

  @Override
  public void send(String phone, String message) {
    try {
      notificationClient.sendSms(new SmsNotificationRequest(phone, message));
    } catch (FeignException exception) {
      throw new ExternalServiceException("SMS notification", exception);
    }
  }
}
