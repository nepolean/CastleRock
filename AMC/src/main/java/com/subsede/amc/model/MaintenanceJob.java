package com.subsede.amc.model;

import java.util.Date;

import com.subsede.amc.model.job.Technician;

public interface MaintenanceJob {

  public void schedule(Date on);

  public void reschedule(Date on);

  public void assign(Technician technician);

  public void start();

  public void pause();

  public void resume();

  public void cancel();

  public void close();

  public void reopen();

  public void complete();

}
