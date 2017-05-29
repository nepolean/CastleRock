package com.real.proj.amc.asset.unit.test;

import java.util.Collection;

import org.statefulj.fsm.Persister;
import org.statefulj.fsm.StaleStateException;
import org.statefulj.fsm.model.State;

import com.real.proj.amc.model.Quotation;

public class InMemoryPersister<T> implements Persister<Quotation> {

  @Override
  public State<Quotation> getCurrent(Quotation paramT) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setCurrent(Quotation paramT, State<Quotation> paramState1, State<Quotation> paramState2)
      throws StaleStateException {
    // TODO Auto-generated method stub

  }

  @Override
  public void setStartState(State<Quotation> paramState) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setStates(Collection<State<Quotation>> paramCollection) {
    // TODO Auto-generated method stub

  }

}
