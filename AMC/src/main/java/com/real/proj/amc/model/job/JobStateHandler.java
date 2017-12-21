package com.real.proj.amc.model.job;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.LifecycleObjectSupport;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class JobStateHandler extends LifecycleObjectSupport {

  private static final Logger logger = LoggerFactory.getLogger(JobStateHandler.class);
  @Autowired
  private StateMachine<JobStates, JobEvents> sm;
  private Set<JobStateChangeListener> listeners = new HashSet<>();
  private JobActionHandler jobActionHandler;

  @Autowired
  public void setStateMachine(StateMachine<JobStates, JobEvents> sm) {
    this.sm = sm;
  }

  @Autowired
  public void setQuotationActionHandler(JobActionHandler handler) {
    this.jobActionHandler = handler;
  }

  @Override
  public void onInit() {
    logger.info("INIT - QuotationStateHandler");
    addKnownHandlers();
    this.sm.getStateMachineAccessor()
        .doWithAllRegions(
            accessor -> accessor.addStateMachineInterceptor(stateMachineInterceptor()));
  }

  private void addKnownHandlers() {
    this.listeners.add(jobActionHandler);
  }

  private StateMachineInterceptor<JobStates, JobEvents> stateMachineInterceptor() {
    return new StateMachineInterceptorAdapter<JobStates, JobEvents>() {
      @Override
      public void preStateChange(
          State<JobStates, JobEvents> state,
          Message<JobEvents> message,
          Transition<JobStates, JobEvents> transition,
          StateMachine<JobStates, JobEvents> stateMachine) {

        for (JobStateChangeListener listener : listeners)
          listener.onStateChange(state, message, transition, stateMachine);
      }
    };
  }

  public void registerListeners(JobStateChangeListener listener) {
    this.listeners.add(listener);
  }

  public void handlEvent(Message<JobEvents> message, JobStates source) throws Exception {
    if (logger.isDebugEnabled())
      logger.debug("> handleEvent on sm id {}, & event {}, & source {}", this.sm.getUuid(), message.getPayload(),
          source);
    this.sm.stop();
    this.sm.setStateMachineError(null);
    this.sm
        .getStateMachineAccessor()
        .doWithAllRegions(accessor -> accessor
            .resetStateMachine(new DefaultStateMachineContext<JobStates, JobEvents>(source, null, null, null)));
    final List<Exception> exception = new LinkedList<Exception>();
    this.sm.addStateListener(new StateMachineListenerAdapter<JobStates, JobEvents>() {
      public void stateMachineError(StateMachine<JobStates, JobEvents> stm, Exception error) {
        logger.info("Notified of the action error");
        exception.add(error);
      }
    });
    this.sm.start();
    if (logger.isDebugEnabled())
      logger.debug("sm has been reset to appropriate state");
    boolean b = false;
    b = this.sm.sendEvent(message);
    if (!b) {
      logger.warn("Action {} not valid for  {} at this time", message.getPayload(), source);
      throw new IllegalArgumentException("Specified action not allowed on this quotation at this time.");
    }
    if (this.sm.hasStateMachineError()) {
      logger.error("the user action has resulted in an error");
      throw exception.get(0);
    }

  }

}
