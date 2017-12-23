package com.subsede.amc.model.job;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import com.subsede.amc.repository.JobRepository;
import com.subsede.util.SecurityHelper;

@Component
public class JobActionHandler implements JobStateChangeListener {

  private static final Logger logger = LoggerFactory.getLogger(JobActionHandler.class);

  private SecurityHelper secHelper;
  private JobRepository jobRepository;

  @Autowired
  public void setSecurityHelper(SecurityHelper secHelper) {
    this.secHelper = secHelper;
  }

  @Autowired
  public void setRepository(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  @Override
  public void onStateChange(
      State<JobStates, JobEvents> state,
      Message<JobEvents> message,
      Transition<JobStates, JobEvents> transition,
      StateMachine<JobStates, JobEvents> stateMachine) {

    JobEvents event = message.getPayload();
    try {
      execute(event, message, transition.getTarget().getId(), stateMachine);
    } catch (Exception ex) {
      logger.error("Error performing action \n{}", ex);
      stateMachine.setStateMachineError(ex);
      throw ex;
    }
  }

  private void execute(
      JobEvents event,
      Message<JobEvents> message,
      JobStates targetState,
      StateMachine<JobStates, JobEvents> stateMachine) {
    logger.info("Event name {}", event.toString());
    switch (event) {
    case ASSIGN:
      handleAssignJob(message, targetState);
      break;
    case SCHEDULE:
      handleScheduleJob(message, targetState);
      break;
    case RESCHEDULE:
      try {
        handleRescheduleJob(message, targetState);
      } catch (Exception ex) {
        stateMachine.setStateMachineError(ex);
      }
      break;
    case START:
      handleStartJob(message, targetState);
      break;
    case PAUSE:
      handlePauseJob(message, targetState);
      break;
    case RESUME:
      handleResumeJob(message, targetState);
      break;
    case COMPLETE:
      handleCompleteJob(message, targetState);
      break;
    case REOPEN:
      handleReopenJob(message, targetState);
      break;
    case AWAIT_PAYMENT:
      handleAwaitPayment(message, targetState);
      break;
    case PAY:
      handlePaymentMade(message, targetState);
      break;
    case CLOSE:
      handleCloseJob(message, targetState);
      break;
    default:
      logger.error("Invalid Event {}", event);
      break;

    }
  }

  private void handleCloseJob(Message<JobEvents> message, JobStates targetState) {
    if (logger.isDebugEnabled())
      logger.debug("Closing the job");
    Map<String, Object> inputData = this.getUserData(message);
    AbstractJob job = this.getJobFromInput(inputData);
    job.close();
    job.setCurrentStatus(targetState);
    this.jobRepository.save(job);
  }

  private void handlePaymentMade(Message<JobEvents> message, JobStates targetState) {

  }

  private void handleAwaitPayment(Message<JobEvents> message, JobStates targetState) {
    if (logger.isDebugEnabled())
      logger.debug("Awaiting payment for the job");
    Map<String, Object> inputData = this.getUserData(message);
    AbstractJob job = this.getJobFromInput(inputData);
    // call order service
    String orderId = null;
    job.setOrderId(orderId);
    job.setCurrentStatus(targetState);
    this.jobRepository.save(job);
  }

  private void handleReopenJob(Message<JobEvents> message, JobStates targetState) {
    if (logger.isDebugEnabled())
      logger.debug("Reopening the job");
    Map<String, Object> inputData = this.getUserData(message);
    AbstractJob job = this.getJobFromInput(inputData);
    job.reopen();
    job.setCurrentStatus(targetState);
    this.jobRepository.save(job);
  }

  private void handleCompleteJob(Message<JobEvents> message, JobStates targetState) {
    if (logger.isDebugEnabled())
      logger.debug("The job is completed");
    Map<String, Object> inputData = this.getUserData(message);
    AbstractJob job = this.getJobFromInput(inputData);
    job.complete();
    job.setCurrentStatus(targetState);
    this.jobRepository.save(job);
  }

  private void handleResumeJob(Message<JobEvents> message, JobStates targetState) {
    if (logger.isDebugEnabled())
      logger.debug("Resuming the job");
    Map<String, Object> inputData = this.getUserData(message);
    AbstractJob job = this.getJobFromInput(inputData);
    job.resume();
    job.setCurrentStatus(targetState);
    this.jobRepository.save(job);
  }

  private void handlePauseJob(Message<JobEvents> message, JobStates targetState) {
    if (logger.isDebugEnabled())
      logger.debug("Pausing the job");
    Map<String, Object> inputData = this.getUserData(message);
    AbstractJob job = this.getJobFromInput(inputData);
    job.pause();
    job.setCurrentStatus(targetState);
    this.jobRepository.save(job);
  }

  private void handleStartJob(Message<JobEvents> message, JobStates targetState) {
    if (logger.isDebugEnabled())
      logger.debug("Starting the job");
    Map<String, Object> inputData = this.getUserData(message);
    AbstractJob job = this.getJobFromInput(inputData);
    job.start();
    job.setCurrentStatus(targetState);
    this.jobRepository.save(job);
  }

  private void handleRescheduleJob(Message<JobEvents> message, JobStates targetState) throws Exception {
    if (logger.isDebugEnabled())
      logger.debug("Reschedulng the job to a specified date");
    Map<String, Object> inputData = this.getUserData(message);
    Date rescheduledDt = (Date) inputData.get(JobConstants.FUTURE);
    // check if the future date falls in a day
    Instant future = rescheduledDt.toInstant();
    Instant now = Instant.now();
    Instant then = now.plus(24, ChronoUnit.HOURS);
    if (future.isBefore(then))
      throw new IllegalArgumentException("You cannot reschedule the job in less than 24hrs.");
    this.handleScheduleJob(message, targetState);
  }

  private void handleScheduleJob(Message<JobEvents> message, JobStates targetState) {
    if (logger.isDebugEnabled())
      logger.debug("Scheduling the job to a specified date");
    Map<String, Object> inputData = this.getUserData(message);
    AbstractJob job = this.getJobFromInput(inputData);
    Date scheduledDt = (Date) inputData.get(JobConstants.FUTURE);
    job.setScheduledDate(scheduledDt);
    job.setCurrentStatus(targetState);
    this.jobRepository.save(job);
  }

  private void handleAssignJob(Message<JobEvents> message, JobStates targetState) {
    if (logger.isDebugEnabled())
      logger.debug("Assigning a technician to the job");
    Map<String, Object> inputData = this.getUserData(message);
    Technician technician = (Technician) inputData.get(JobConstants.TECHNICIAN);
    AbstractJob job = getJobFromInput(inputData);
    job.assign(technician);
    job.setCurrentStatus(targetState);
    this.jobRepository.save(job);
  }

  private AbstractJob getJobFromInput(Map<String, Object> inputData) {
    return (AbstractJob) inputData.get(JobConstants.JOB);
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> getUserData(Message<JobEvents> message) {
    Map<String, Object> userData = (Map<String, Object>) message.getHeaders().get("DATA_KEY");
    userData = Optional.of(userData).orElse(new HashMap<String, Object>(1));
    return userData;
  }

}
