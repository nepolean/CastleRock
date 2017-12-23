package com.subsede.amc.model.job;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.stereotype.Component;
@Configuration
@EnableStateMachine(name="JobFlow")
public class JobSMConfig extends EnumStateMachineConfigurerAdapter<JobStates, JobEvents> {

  private static Logger logger = LoggerFactory.getLogger(JobSMConfig.class);

  public void configure(StateMachineStateConfigurer<JobStates, JobEvents> states) throws Exception {
    logger.info("Configuring job workflow");
    states
        .withStates()
        .initial(JobStates.INITIAL)
        .states(EnumSet.allOf(JobStates.class));
  }

  public void configure(StateMachineTransitionConfigurer<JobStates, JobEvents> transitions) throws Exception {
    //@formatter:off
    transitions
      .withExternal()
        .source(JobStates.INITIAL).target(JobStates.CREATED)
        .event(JobEvents.CREATE_JOB)
        //.action(action1())
      .and()
      .withExternal()
        .source(JobStates.CREATED).source(JobStates.REOPENED).target(JobStates.ASSIGNED)
        .event(JobEvents.ASSIGN)
        //.action(action1())
      .and()
      .withExternal()
      .source(JobStates.ASSIGNED).target(JobStates.SCHEDULED)
      .event(JobEvents.SCHEDULE)
      //.action(action1())
      .and()
      .withExternal()
      .source(JobStates.SCHEDULED).target(JobStates.SCHEDULED)
      .event(JobEvents.RESCHEDULE)
      //.action(action1())
      .and()      
      .withExternal()
        .source(JobStates.SCHEDULED).target(JobStates.STARTED)
        .event(JobEvents.START)
        //.action(action1())
      .and()
      .withExternal()
        .source(JobStates.STARTED).target(JobStates.PAUSED)
        .event(JobEvents.PAUSE)
      .and()
      .withExternal()
        .source(JobStates.PAUSED).target(JobStates.RESUMED)
        .event(JobEvents.RESUME)
      .and()
      .withExternal()
        .source(JobStates.STARTED).source(JobStates.RESUMED)
        .target(JobStates.COMPLETED)
        .event(JobEvents.COMPLETE)
      .and()
      .withExternal()
        .source(JobStates.COMPLETED).target(JobStates.CLOSED)
        .event(JobEvents.CLOSE)
      .and()
      .withExternal()
        .source(JobStates.COMPLETED).target(JobStates.REOPENED)
        .event(JobEvents.REOPEN)          
      .and()
      .withExternal()
        .source(JobStates.COMPLETED).target(JobStates.PAYMENT_PENDING)
        .event(JobEvents.AWAIT_PAYMENT)
      .and()
      .withExternal()
        .source(JobStates.PAYMENT_PENDING).target(JobStates.CLOSED)
        .event(JobEvents.PAY);
          //.action(action1())  
  }

}
