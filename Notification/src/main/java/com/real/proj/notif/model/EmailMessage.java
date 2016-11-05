package com.real.proj.notif.model;

import java.util.List;

import javax.validation.constraints.NotNull;

public class EmailMessage implements java.io.Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @NotNull(message = "The e-mail address cannot be empty")
  private String to;
  private String subject;
  private List<String> ccList;
  @NotNull (message = "The email body cannot be empty")
  private String message;
  
  
  public EmailMessage() {
    
  }
  
  public EmailMessage(String to, String subject, List<String> ccList, String message) {
    super();
    this.to = to;
    this.subject = subject;
    this.ccList = ccList;
    this.message = message;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public List<String> getCcList() {
    return ccList;
  }

  public void setCcList(List<String> ccList) {
    this.ccList = ccList;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "EmailMessage [to=" + to + ", subject=" + subject + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ccList == null) ? 0 : ccList.hashCode());
    result = prime * result + ((message == null) ? 0 : message.hashCode());
    result = prime * result + ((subject == null) ? 0 : subject.hashCode());
    result = prime * result + ((to == null) ? 0 : to.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EmailMessage other = (EmailMessage) obj;
    if (ccList == null) {
      if (other.ccList != null)
        return false;
    } else if (!ccList.equals(other.ccList))
      return false;
    if (message == null) {
      if (other.message != null)
        return false;
    } else if (!message.equals(other.message))
      return false;
    if (subject == null) {
      if (other.subject != null)
        return false;
    } else if (!subject.equals(other.subject))
      return false;
    if (to == null) {
      if (other.to != null)
        return false;
    } else if (!to.equals(other.to))
      return false;
    return true;
  }

  
}
