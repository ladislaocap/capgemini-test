package com.capgemini.test.code.application.port.out;

public interface SmsNotifier {
  void send(String phone, String message);
}
