package com.subsede.amc.model.job;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.subsede.amc.catalog.model.BaseMasterEntity;
import com.subsede.amc.catalog.model.ServiceType;
import com.subsede.amc.model.EventHistory;
import com.subsede.amc.model.MaintenanceJob;
import com.subsede.user.model.user.User;

@Document(collection = "Jobs")
public class AbstractJob extends BaseMasterEntity implements MaintenanceJob {

  @Id
  private String id;
  private String name;
  /* service related information */
  private List<String> skills;
  /* source of this request */
  private String sourceType;
  private String sourceId;
  @DBRef
  private User customer;
  private Date scheduledDate;
  private Technician technician;
  private JobStates currentStatus;
  private Date lastStatusUpdate;
  private List<EventHistory> history;
  private String orderId;

  public AbstractJob(
      String name,
      List<String> serviceType,
      String sourceType,
      String sourceId,
      User customer) {
    this.name = name;
    this.skills = serviceType;
    this.sourceType = sourceType;
    this.sourceId = sourceId;
    this.customer = customer;
    this.currentStatus = JobStates.CREATED;
  }

  @Override
  public void schedule(Date on) {
    this.scheduledDate = on;
    this.lastStatusUpdate = new Date();
  }

  @Override
  public void reschedule(Date on) {
    this.scheduledDate = on;
    this.lastStatusUpdate = new Date();
  }

  @Override
  public void assign(Technician technician) {
    this.technician = technician;
    this.lastStatusUpdate = new Date();
  }

  @Override
  public void start() {
    this.currentStatus = JobStates.STARTED;
    this.lastStatusUpdate = new Date();
  }

  @Override
  public void pause() {
    this.currentStatus = JobStates.PAUSED;
    this.lastStatusUpdate = new Date();
  }

  @Override
  public void resume() {
    this.currentStatus = JobStates.RESUMED;
    this.lastStatusUpdate = new Date();
  }

  @Override
  public void cancel() {
    this.currentStatus = JobStates.CANCELLED;
    this.lastStatusUpdate = new Date();
  }

  @Override
  public void close() {
    this.currentStatus = JobStates.CANCELLED;
    this.lastStatusUpdate = new Date();
  }

  @Override
  public void reopen() {
    this.currentStatus = JobStates.REOPENED;
    this.lastStatusUpdate = new Date();
  }

  @Override
  public void complete() {
    this.currentStatus = JobStates.COMPLETED;
    this.lastStatusUpdate = new Date();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getSkills() {
    return skills;
  }

  public void setSkills(List<String> serviceType) {
    this.skills = serviceType;
  }

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public Date getScheduledDate() {
    return scheduledDate;
  }

  public void setScheduledDate(Date scheduledDate) {
    this.scheduledDate = scheduledDate;
  }

  public Technician getTechnician() {
    return technician;
  }

  public void setTechnician(Technician technician) {
    this.technician = technician;
  }

  public JobStates getCurrentStatus() {
    return currentStatus;
  }

  public void setCurrentStatus(JobStates currentStatus) {
    this.currentStatus = currentStatus;
  }

  public Date getLastStatusUpdate() {
    return lastStatusUpdate;
  }

  public void setLastStatusUpdate(Date lastStatusUpdate) {
    this.lastStatusUpdate = lastStatusUpdate;
  }

  public List<EventHistory> getHistory() {
    return history;
  }

  public void setHistory(List<EventHistory> history) {
    this.history = history;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getOrderId() {
    return this.orderId;
  }

}
