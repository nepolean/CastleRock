package com.real.proj.amc.model;

import java.util.Date;

import com.real.proj.user.model.User;

public interface MaintenanceJob {

  public void schedule(Date on);

  public void reschedule(Date on);

  public void assign(User technician);

  public void start();

  public void pause();

  public void resume();

  public void cancel();

  public void close();

  public void reopen();

  public void complete();

}
