package com.real.proj.notif.service;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static com.real.proj.notif.util.ValidationHelper.*;

public class NotificationService {
  
  @Autowired
  private static JavaMailSender mailSender;

  public static void sendEmail(String to, List<String> ccList, String subject, String message) 
    throws Exception {
    
    if (isEmpty(to) || isEmpty(message)) 
      throw new IllegalArgumentException("Invalid address and/or message");
      
    MimeMessage mail = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(mail, true);
      String sub = sanitize(subject);
      helper.setTo(to);
      helper.setSubject(sub);
      if (!isEmpty(ccList)) {
        for(String cc : ccList)
          if(!isEmpty(cc))
            helper.setCc(cc);
      }
      helper.setText(message);
    } catch(MessagingException ex) {
      ex.printStackTrace(System.err);
      throw new Exception("Error while constructing the mail message");
    }
    
    try {
      mailSender.send(mail);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error while sending the mail");
    }
  }

}
