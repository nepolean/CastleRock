package com.subsede.amc.model.quote;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

public interface QuotationStateChangeListener {

  public void onStateChange(
      State<QStates, QEvents> state,
      Message<QEvents> message,
      Transition<QStates, QEvents> transition,
      StateMachine<QStates, QEvents> stateMachine);

}
