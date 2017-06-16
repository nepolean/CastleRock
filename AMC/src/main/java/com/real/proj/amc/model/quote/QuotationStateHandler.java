package com.real.proj.amc.model.quote;

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
public class QuotationStateHandler extends LifecycleObjectSupport {

  private static final Logger logger = LoggerFactory.getLogger(QuotationStateHandler.class);
  @Autowired
  private StateMachine<QStates, QEvents> sm;
  private Set<QuotationStateChangeListener> listeners = new HashSet<>();
  private QuotationActionHandler quoteActionHandler;

  @Autowired
  public void setStateMachine(StateMachine<QStates, QEvents> sm) {
    this.sm = sm;
  }

  @Autowired
  public void setQuotationActionHandler(QuotationActionHandler handler) {
    this.quoteActionHandler = handler;
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
    this.listeners.add(quoteActionHandler);
  }

  private StateMachineInterceptor<QStates, QEvents> stateMachineInterceptor() {
    return new StateMachineInterceptorAdapter<QStates, QEvents>() {
      @Override
      public void preStateChange(
          State<QStates, QEvents> state,
          Message<QEvents> message,
          Transition<QStates, QEvents> transition,
          StateMachine<QStates, QEvents> stateMachine) {

        for (QuotationStateChangeListener listener : listeners)
          listener.onStateChange(state, message, transition, stateMachine);
      }
    };
  }

  public void registerListeners(QuotationStateChangeListener listener) {
    this.listeners.add(listener);
  }

  public void handlEvent(Message<QEvents> message, QStates source) throws Exception {
    if (logger.isDebugEnabled())
      logger.debug("> handleEvent on sm id {}, & event {}, & source {}", this.sm.getUuid(), message.getPayload(),
          source);
    this.sm.stop();
    this.sm.setStateMachineError(null);
    this.sm
        .getStateMachineAccessor()
        .doWithAllRegions(accessor -> accessor
            .resetStateMachine(new DefaultStateMachineContext<QStates, QEvents>(source, null, null, null)));
    final List<Exception> exception = new LinkedList<Exception>();
    this.sm.addStateListener(new StateMachineListenerAdapter<QStates, QEvents>() {
      public void stateMachineError(StateMachine<QStates, QEvents> stm, Exception error) {
        logger.info("Notified of the action error");
        exception.add(error);
      }
    });
    this.sm.start();
    if (logger.isDebugEnabled())
      logger.debug("sm has been reset to appropriate state");
    boolean b = false;
    b = this.sm.sendEvent(message);
    if (!b)
      logger.warn("state transition not occured");
    if (this.sm.hasStateMachineError()) {
      logger.error("the user action has resulted in an error");
      throw exception.get(0);
    }
  }

}
