package com.capgemini.test.code.application.port.out;

public interface EmailNotifier {
  void send(String email, String message);
}
