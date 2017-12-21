package com.subsede.amc.model.quote;

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
        .event(QEvents.CREATE_QUOTE)
        //.action(action1())
      .and()
      .withExternal()
        .source(QStates.NEW_QUOTATION_CREATED).target(QStates.QUOTATION_GENERATED)
        .event(QEvents.GENERATE_QUOTE)
        //.action(action1())
      .and()
      .withExternal()
        .source(QStates.QUOTATION_GENERATED).target(QStates.USER_ACCEPTED)
        .event(QEvents.ACCEPT)
        //.action(action1())
      .and()
      .withExternal()
        .source(QStates.QUOTATION_GENERATED).target(QStates.USER_REJECTED)
        .event(QEvents.REJECT)
      .and()
      .withExternal()
        .source(QStates.USER_ACCEPTED).target(QStates.QUOTATION_REGENERATED)
        .event(QEvents.REGENERATE)
      .and()
      .withExternal()
        .source(QStates.USER_REJECTED).target(QStates.QUOTATION_REGENERATED)
        .event(QEvents.REGENERATE)
      .and()
      .withExternal()
        .source(QStates.QUOTATION_REGENERATED).target(QStates.PAYMENT_PENDING)
        .event(QEvents.ACCEPT)
      .and()
      .withExternal()
        .source(QStates.QUOTATION_REGENERATED).target(QStates.USER_REJECTED)
        .event(QEvents.REJECT)          
      .and()
      .withExternal()
        .source(QStates.USER_ACCEPTED).target(QStates.PAYMENT_PENDING)
        .event(QEvents.APPROVE)
      .and()
      .withExternal()
        .source(QStates.USER_ACCEPTED).target(QStates.QUOTATION_REJECTED)
        .event(QEvents.REJECT)
          //.action(action1())        
      .and()
      .withExternal()
        .source(QStates.PAYMENT_PENDING).target(QStates.PAYMENT_INITIATED)
        .event(QEvents.INITIATE_PAY)
      .and()
      .withExternal()
        .source(QStates.PAYMENT_INITIATED).target(QStates.SUBSCRIBED)
        .event(QEvents.PAYMENT_SUCCESSFUL)
        //.action(action1());
      .and()
      .withExternal()
        .source(QStates.SUBSCRIBED).target(QStates.SUBSCRIBED)
        .event(QEvents.PAYMENT_SUCCESSFUL)
      .and()
      .withExternal()
        .source(QStates.PAYMENT_INITIATED).target(QStates.PAYMENT_PENDING)
        .event(QEvents.PAYMENT_ERROR)        
      .and() 
      .withExternal()
        .source(QStates.SUBSCRIBED).target(QStates.SUBSCRIPTION_EXPIRED)
        .event(QEvents.EXPIRE)        
      .and()
      .withExternal()
        .source(QStates.QUOTATION_GENERATED).source(QStates.QUOTATION_APPROVED).source(QStates.QUOTATION_REGENERATED)
        .source(QStates.USER_ACCEPTED).target(QStates.QUOTATION_EXPIRED)
        .event(QEvents.EXPIRE)
      .and()
      .withExternal()
        .source(QStates.QUOTATION_EXPIRED).target(QStates.QUOTATION_REGENERATED)
        .event(QEvents.RENEW);        
  }

}
