package com.real.proj.amc.model.quote;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class QuotationSpringSMConfig extends EnumStateMachineConfigurerAdapter<QStates, QEvents> {

  private static Logger logger = LoggerFactory.getLogger(QuotationSpringSMConfig.class);

  public void configure(StateMachineStateConfigurer<QStates, QEvents> states) throws Exception {
    logger.info("configuring quotation workflow");
    states
        .withStates()
        .initial(QStates.INITIAL)
        .states(EnumSet.allOf(QStates.class));
  }

  public void configure(StateMachineTransitionConfigurer<QStates, QEvents> transitions) throws Exception {
    //@formatter:off
    transitions
      .withExternal()
        .source(QStates.INITIAL).target(QStates.NEW_QUOTATION_CREATED)
        .event(QEvents.CREATE)
        //.action(action1())
      .and()
      .withExternal()
        .source(QStates.NEW_QUOTATION_CREATED).target(QStates.QUOTATION_GENERATED)
        .event(QEvents.SUBMITUSERDATA)
        //.action(action1())
      .and()
      .withExternal()
        .source(QStates.NEW_QUOTATION_CREATED).target(QStates.USER_ACCEPTED)
        .event(QEvents.ACCEPT)
        //.action(action1())
      .and()
      .withExternal()
        .source(QStates.USER_ACCEPTED).target(QStates.QUOTATION_APPROVED)
        .event(QEvents.APPROVE)
        //.action(action1())
      .and()
      .withExternal()
        .source(QStates.QUOTATION_APPROVED).target(QStates.PAYMENT_MADE)
        .event(QEvents.PAY)
        //.action(action1())
      .and()
      .withLocal()
        .source(QStates.PAYMENT_MADE).target(QStates.SUBSCRIBED);
        //.action(action1());
        
  }

}
