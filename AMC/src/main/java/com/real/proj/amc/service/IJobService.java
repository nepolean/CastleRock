package com.real.proj.amc.service;

import java.util.Date;

import com.real.proj.amc.model.job.Technician;

public interface IJobService {

  public void schedule(Date to);

  public void assign(Technician technician);

  public void start();

  public void pause(String reason);

  public void resume(String msg);

  public void cancel(String msg);

  public void complete();

  public void reject(String reason);

  public void close();

  public void reopen();

}
