package com.be.server.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;


public class EmailService {

    public static void sendEmail(String to, String subject, String content) {
        // Cấu hình các thuộc tính của email
        String host = "smtp.gmail.com";  // Ví dụ sử dụng Gmail SMTP server
        String from = "shopglamsole@gmail.com";  // Địa chỉ email của bạn
        String password = "etem eirl ebtj jxln";  // Mật khẩu email của bạn

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Khởi tạo session để gửi email
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Tạo email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            // Gửi email
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
