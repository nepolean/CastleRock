package com.subsede.notification.service;

import static com.subsede.notification.util.ValidationHelper.isEmpty;
import static com.subsede.notification.util.ValidationHelper.sanitize;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	
	private static Logger logger = LogManager.getLogger(NotificationService.class);
  
  @Autowired
  private JavaMailSender mailSender;
  
  public NotificationService() {
    
  }

  public  void sendEmail(String to, List<String> ccList, String subject, String message) 
    throws Exception {
    if (logger.isDebugEnabled())
    	logger.debug("send email to " + to);
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
    	logger.error("Error while constructing the email message for " + to, ex);
      throw new Exception("Error while constructing the mail message");
    }
    
    try {
      mailSender.send(mail);
    } catch (Exception ex) {
    	logger.error("Error while sending the email message for " + to, ex);
      throw new Exception("Error while sending the mail");
    }
  }

  public void sendSMS(String mobile, String message) {
    // TODO Consider using Twilio
    
  }

}
