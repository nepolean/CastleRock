package com.real.proj.amc.service;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import com.real.proj.amc.model.Events;
import com.real.proj.amc.model.States;

@Configuration
@EnableStateMachine
public class SubscriptionWorkflow extends EnumStateMachineConfigurerAdapter<States, Events> {

  public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {

    states.withStates().initial(States.CREATED).states(EnumSet.allOf(States.class));

  }

  public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
      throws Exception {
    transitions
        .withExternal()
        .source(States.CREATED)
        .target(States.QUOTATION_SENT)
        .event(Events.rate)
        .and()
        .withExternal()
        .source(States.QUOTATION_SENT)
        .target(States.PAID)
        .event(Events.payment_receive)
        .and()
        .withExternal()
        .source(States.PAID)
        .target(States.COMPLETED)
        .event(Events.approve)
        .and()
        .withInternal()
        .source(States.QUOTATION_SENT)
        .timer(1000)
        .action(notifyUser());

  }

  private Guard<States, Events> guardRateAll() {
    return new Guard<States, Events>() {
      @Override
      public boolean evaluate(StateContext<States, Events> paramStateContext) {
        return false;
      }
    };
  }

  private Action<States, Events> notifyUser() {
    return new Action<States, Events>() {

      @Override
      public void execute(StateContext<States, Events> context) {
        String id = (String) context.getExtendedState().getVariables().get("USER");
        // Message<Events> msg = context.getMessage();
        // String id = (String) msg.getHeaders().get("USER");
        System.out.println("Notifying %%%%%%%%%%%%%%%%%%%% " + id);
        // System.out.println("Throwing error.......................");
        // throw new RuntimeException();
      }
    };
  }

}
