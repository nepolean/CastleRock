package com.real.proj.amc.asset.unit.test;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.statefulj.fsm.FSM;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.fsm.model.State;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.Location;
import com.real.proj.amc.model.Product;
import com.real.proj.amc.model.Quotation;
import com.real.proj.amc.model.Quotation.Event;
import com.real.proj.amc.model.Quotation.States;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.Tax;
import com.real.proj.amc.model.UserData;
import com.real.proj.amc.model.UserInput;
import com.real.proj.amc.unit.test.ServiceTestHelper;
import com.real.proj.user.model.User;

public class QuotationTest {

  private Logger logger = LoggerFactory.getLogger(QuotationTest.class);

  static {
    System.setProperty("ENVIRONMENT", "TEST");
  }

  @Test
  public void testQuoteGeneration() {
    Quotation quote = createQuotation();
    Product product = ServiceTestHelper.createPackageWithDiscount();
    int tenure = 4;
    double measuredArea = 1000.0;
    Rating four = Rating.FOUR;
    UserData data = new UserData(product, four, tenure, measuredArea);
    quote.setUserData(data);
    quote.setApplicableTaxes(this.createTaxes());
    AMCPackage sampleProduct = ServiceTestHelper.createPackageWithDiscount();
    UserInput<String, Object> input = new UserInput<String, Object>();
    input.add(Rating.getKey(), four);
    double price = sampleProduct.getActualPrice(input).getSubscriptionPrice();
    quote.createQuote();
    logger.info(quote.toString());
    /* services(2) * tenure(4) * measuredArea(1000) * servicecost(160) */
    double expectedValue = tenure * measuredArea * price;
    double expectedDisc = expectedValue * (20.0 / 100);
    assert (Double.compare(expectedValue, quote.getTotalAmount()) == 0.0);
    assert (Double.compare(expectedDisc, quote.getDiscount()) == 0.0);

  }

  @Test
  public void setUserModification() {
    Quotation quote = this.createQuotation();
    Product product = ServiceTestHelper.createPackageWithDiscount();
    UserData data = new UserData(product, Rating.FIVE, 4, 1000.0);
    quote.setUserData(data);
    quote.setApplicableTaxes(this.createTaxes());
    quote.createQuote();
    double before = quote.getNetAmount();
    logger.info("Before {}", before);
    data = quote.getUserData();
    data.setMeasuredArea(1200.0);
    quote.modifyUserData(data);
    double after = quote.getNetAmount();
    logger.info("After {}", after);

    assertEquals(20.0, (after - before) * 100 / before, 0.01);
  }

  @Test
  public void testWorkflow() {
    FSM<Quotation> fsm = setUpWorkflow();
    Quotation quote = this.createQuotation();
    State<Quotation> currentState = fsm.getCurrentState(quote);
    assertEquals(States.INIT.toString(), currentState.getName());
    UserData data = new UserData(ServiceTestHelper.createPackageWithDiscount(), Rating.ONE, 2, 1000.0);
    try {
      fsm.onEvent(quote, Event.GENERATE_QUOTE.toString(), data);
      assertEquals(States.QUOTE_GENERATED.toString(), quote.getState());
      fsm.onEvent(quote, Event.ACCEPT_QUOTE.toString(), "AcceptedByME");
      assertEquals(States.QUOTE_ACCEPTED.toString(), quote.getState());
      data.setRating(Rating.TWO);
      fsm.onEvent(quote, Event.REGENERATE_QUOTE.toString(), data);
      assertEquals(States.QUOTE_REGENERATED.toString(), quote.getState());
      fsm.onEvent(quote, Event.ACCEPT_QUOTE.toString(), "I Accept");
      assertEquals(States.QUOTE_APPROVED.toString(), quote.getState());
      // fsm.onEvent(quote, Quotation.approveQuote, "");
      // assertEquals(Quotation.quote_approved.getName(), quote.getState());
      fsm.onEvent(quote, Event.PAYMENT_RECEIVED.toString(), "");
      assertEquals(States.PAYMENT_RECEIVED.toString(), quote.getState());
      State<Quotation> funnyThing = fsm.onEvent(quote, Event.APPROVE_QUOTE.toString(), "");
      logger.info("Funny state = {}", funnyThing);

    } catch (TooBusyException ex) {
      ex.printStackTrace();
    }

  }

  @Test
  public void testAltFlow() {

    Quotation quote = this.createQuotation();
    FSM<Quotation> fsm = this.setUpWorkflow();
    // fsm.onEvent(quote, event, args)

  }

  private FSM<Quotation> setUpWorkflow() {
    FSM<Quotation> fsm = new FSM<Quotation>("QuotationFlow");
    List<State<Quotation>> states = new LinkedList<State<Quotation>>();
    for (States state : States.values()) {
      states.add(state.get());
    }
    fsm.setPersister(new MemoryPersisterImpl<Quotation>(states, States.INIT.get(), "state"));
    return fsm;
  }

  private Quotation createQuotation() {
    User newUser = this.createUser();
    Location location = this.createLocation();
    Quotation quote = new Quotation(newUser, newUser, location);
    return quote;
  }

  private Set<Tax> createTaxes() {
    Set<Tax> taxes = new HashSet<Tax>();
    taxes.add(new Tax("Service Tax", 12.0));
    taxes.add(new Tax("Service Charge", 5.0));

    return taxes;
  }

  private Location createLocation() {
    return new Location();
  }

  private User createUser() {
    return new User("ABC", "XYZ", "abc@gmail.com", "1111111111");
  }

}
