package com.real.proj.amc.asset.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.test.context.TestPropertySource;
import org.statefulj.fsm.FSM;
import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.Location;
import com.real.proj.amc.model.Product;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.Tax;
import com.real.proj.amc.model.UserData;
import com.real.proj.amc.model.UserInput;
import com.real.proj.amc.model.quote.Quotation;
import com.real.proj.amc.model.quote.QuotationConfig;
import com.real.proj.amc.model.quote.QuotationConfig.States;
import com.real.proj.amc.model.quote.QuotationRepository;
import com.real.proj.amc.model.quote.QuotationSpringSMConfig;
import com.real.proj.amc.model.quote.QuotationSpringSMConfig.Events;
import com.real.proj.amc.model.quote.QuotationSpringSMConfig.QuoteStates;
import com.real.proj.amc.model.quote.QuotationStateHandler;
import com.real.proj.amc.unit.test.ServiceTestHelper;
import com.real.proj.user.model.User;

@TestPropertySource(properties = "logging.level.org.springframework:INFO")
public class QuotationTest {

  private Logger logger = LoggerFactory.getLogger(QuotationTest.class);
  private QuotationConfig config;
  static {
    System.setProperty("ENVIRONMENT", "TEST");
  }

  @Before
  public void setUp() {
    System.setProperty("logging.level.org.springframework", "INFO");
    QuotationRepository quoteRepo = Mockito.mock(QuotationRepository.class);
    QuotationStateHandler handler = new QuotationStateHandler();
    handler.setQuotationRepository(quoteRepo);
    config = new QuotationConfig();
    config.setQuotationActionHandler(handler);
    config.setActionHandler(handler);
  }

  @Test
  public void testQuoteGeneration() {
    Quotation quote = createQuotation();
    Product product = ServiceTestHelper.createPackageWithDiscount();
    int tenure = 4;
    double measuredArea = 1000.0;
    Rating four = Rating.FOUR;
    UserData data = new UserData(3, tenure, measuredArea);
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
    Calendar today = Calendar.getInstance();
    today.add(Calendar.DAY_OF_MONTH, 30);
    Calendar validUpto = Calendar.getInstance();
    validUpto.setTime(quote.getValidUpto());
    assertEquals(today.get(Calendar.DAY_OF_MONTH), validUpto.get(Calendar.DAY_OF_MONTH));
  }

  @Test
  public void setUserModification() {
    Quotation quote = this.createQuotation();
    Product product = ServiceTestHelper.createPackageWithDiscount();
    UserData data = new UserData(30, 4, 1000.0);
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
    assertEquals(States.INIT.get(), currentState);
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
      fsm.onEvent(quote, Event.RECEIVE_PAYMENT.toString(), "");
      assertEquals(States.PAYMENT_RECEIVED.toString(), quote.getState());
      State<Quotation> funnyThing = fsm.onEvent(quote, Event.APPROVE_QUOTE.toString(), "");
      logger.info("Funny state = {}", funnyThing);
      logger.info("{}", quote.getHistory());
    } catch (TooBusyException ex) {
      ex.printStackTrace();
    }

  }

  @Test
  public void testAltFlow() {

    Quotation quote = this.createQuotation();
    FSM<Quotation> fsm = this.setUpWorkflow();
    try {
      UserData data = new UserData(ServiceTestHelper.createPackageWithDiscount(), Rating.ONE, 2, 1000.0);
      fsm.onEvent(quote, Event.GENERATE_QUOTE.toString(), data);
      fsm.onEvent(quote, Event.ACCEPT_QUOTE.toString(), "");
      fsm.onEvent(quote, Event.APPROVE_QUOTE.toString(), "");
      assertEquals(States.QUOTE_APPROVED.toString(), quote.getState());

    } catch (TooBusyException ex) {
      ex.printStackTrace();
      fail("The test has failed with the Exception");
    }

  }

  @Test
  public void testActionErrorShouldResetState() {
    config.setActionHandler(new MalformedActionHandler());
    Quotation quote = this.createQuotation();
    FSM<Quotation> fsm = this.setUpWorkflow();
    UserData data = new UserData(ServiceTestHelper.createPackageWithDiscount(), Rating.ONE, 2, 1000.0);
    try {
      fsm.onEvent(quote, Event.GENERATE_QUOTE.toString(), data);
      fail("Error in action should prevent flow coming here.");
    } catch (TooBusyException ex) {
      ex.printStackTrace();
    } catch (RuntimeException ex) {
      ex.printStackTrace();
    }
    assertEquals(States.INIT.get(), fsm.getCurrentState(quote));
  }

  @Test
  public void testSpringStateMachine() throws Exception {
    StateMachine<QuoteStates, Events> sm = QuotationSpringSMConfig.createDefaultMachine();
    sm.getStateMachineAccessor()
        .doWithAllRegions(
            func -> func.addStateMachineInterceptor(new StateMachineInterceptorAdapter<QuoteStates, Events>() {
              @Override
              public Exception stateMachineError(StateMachine<QuoteStates, Events> sm, Exception ex) {
                logger.error("*********************  Error during transition {}", ex);
                return null;
              }
            }));
    sm.start();

    logger.info("Sending Event");
    Message<Events> msg = MessageBuilder
        .withPayload(Events.CREATE)
        .setHeader("Greeting", "Hellooooo")
        .build();
    boolean b = sm.sendEvent(msg);
    logger.info("whether the transition occured ******************* {} current state is {}", sm.hasStateMachineError(),
        sm.getState().getId());

    logger.info("Sending the same event again..");

    boolean b1 = sm.sendEvent(msg);
    logger.info("whether the transition occured ******************* {} current state is {}", sm.hasStateMachineError(),
        sm.getState().getId());
  }

  @Test
  public void testCreateSpringStateMachineOnTheFly() throws Exception {
    QuoteStates currentState = QuoteStates.NEW_QUOTATION_CREATED;
    QuotationSpringSMConfig configurer = new QuotationSpringSMConfig();
    StateMachine<QuoteStates, Events> sm = configurer.createStateMachine(currentState);
    sm.start();
    logger.info("{}", sm.getState().getId());
    boolean b = sm.sendEvent(Events.SUBMITUSERDATA);
    logger.info("SM Triggered ? {}", b);
  }

  @Test
  public void testParallelExecution() throws Exception {
    long before = Runtime.getRuntime().freeMemory();
    IntStream.range(1, 10000).parallel().forEach(i -> {
      try {
        logger.info("Current thread count &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& {}", i);
        StateMachine<QuoteStates, Events> sm = QuotationSpringSMConfig.createDefaultMachine();

        StateMachine<QuoteStates, Events> currSM = QuotationSpringSMConfig.resteToState(sm,
            QuoteStates.NEW_QUOTATION_CREATED);
        boolean accepted = currSM.sendEvent(Events.SUBMITUSERDATA);
        assertEquals(true, accepted);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
    long after = Runtime.getRuntime().freeMemory();
    logger.info(" The diff is  {} ", (after - before));

  }

  private FSM<Quotation> setUpWorkflow() {
    FSM<Quotation> fsm = new FSM<Quotation>("QuotationFlow");
    List<State<Quotation>> states = new LinkedList<State<Quotation>>();

    for (States state : States.values()) {
      states.add(state.get());
    }
    fsm.setPersister(new MemoryPersisterImpl<Quotation>(states, States.INIT.get(), "state"));
    logger.debug("{}", fsm);
    return fsm;
  }

  private Quotation createQuotation() {
    User newUser = this.createUser();
    Location location = this.createLocation();
    Quotation quote = new Quotation(newUser, newUser, location);
    quote.setApplicableTaxes(this.createTaxes());
    return quote;
  }

  private List<Tax> createTaxes() {
    List<Tax> taxes = new LinkedList<Tax>();
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

  static class MalformedActionHandler implements Action<Quotation> {

    @Override
    public void execute(Quotation paramT, String paramString, Object... paramArrayOfObject) throws RetryException {
      throw new RetryException("Action errors should reset the state");
    }

  }

}
