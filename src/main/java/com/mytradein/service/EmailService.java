package com.mytradein.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String body);
}