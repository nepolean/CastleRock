package com.real.proj.amc.model.job;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

public interface JobStateChangeListener {

  public void onStateChange(
      State<JobStates, JobEvents> state,
      Message<JobEvents> message,
      Transition<JobStates, JobEvents> transition,
      StateMachine<JobStates, JobEvents> stateMachine);

}
