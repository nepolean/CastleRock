package com.real.proj.amc.model.subscription;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.real.proj.amc.model.AbstractJob;
import com.real.proj.amc.model.Asset;
import com.real.proj.amc.model.AssetBasedJob;
import com.real.proj.amc.model.Product;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.Service;
import com.real.proj.amc.model.SubscriptionData;
import com.real.proj.amc.model.UserData;
import com.real.proj.amc.model.asset.AssetBasedService;
import com.real.proj.amc.model.asset.RatingBasedSubscriptionData;
import com.real.proj.amc.model.quote.Quotation;
import com.real.proj.amc.repository.JobRepository;
import com.real.proj.user.model.User;

@Component
public class SubscriptionJobScheduler {

  private static Logger logger = LoggerFactory.getLogger(SubscriptionJobScheduler.class);
  private JobRepository jobRepository;

  @Autowired
  public void setJobRepository(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  public void scheduleJob(Subscription subscription) {
    logger.info("Schedule jobs for subscription with id {}", subscription.getId());
    Set<Product> services = subscription.getProducts();
    List<AbstractJob> jobs = new LinkedList<>();
    for (Product service : services) {
      SubscriptionData serviceData = service.fetchSubscriptionData(subscription.getUserData().getInput());
      int visitsPerQuarter = serviceData.getVisitCount();
      int noOfQuarters = subscription.getUserData().getTenure();
      int expected = visitsPerQuarter * noOfQuarters;
      schedule(subscription, (Service) service, jobs, visitsPerQuarter, noOfQuarters);
      if (expected != jobs.size())
        throw new IllegalStateException("Something went wrong with the system");
    }
    this.jobRepository.save(jobs);
  }

  private void schedule(
      Subscription subscription,
      Service service,
      List<AbstractJob> jobs,
      int visitsPerQuarter,
      int noOfQuarters) {

    int noOfDays = 90; // per quarter
    int durationBetweenServices = noOfDays / visitsPerQuarter;
    int start = durationBetweenServices;
    for (int i = 0; i < noOfQuarters; i++) {
      for (int j = 0; j < visitsPerQuarter; j++) {
        Date after = getDateAfter(start);
        if (logger.isDebugEnabled())
          logger.debug("Job for service {} is scheduled on {}", service.getName(), after);
        AbstractJob job = new AssetBasedJob(
            service.getName(),
            subscription.getAsset(),
            service.getServiceType(),
            subscription.getClass().getName(),
            subscription.getId());
        jobs.add(job);
        start += durationBetweenServices;
      }
    }
  }

  private Date getDateAfter(int start) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, start);
    return cal.getTime();
  }

  public static void main(String[] args) {
    System.setProperty("ENVIRONMENT", "TEST");
    User user = new User("First", "Last", "first@gmail.com", "9999999");
    Asset asset = new Asset(user);
    Service service = new AssetBasedService("A", "ABC", null, null, null);
    Map<Rating, SubscriptionData> data = new HashMap<Rating, SubscriptionData>();
    for (Rating key : Rating.values())
      data.put(key, new SubscriptionData(100.0, 3));
    service.setSubscriptionData(new RatingBasedSubscriptionData(data));
    Quotation quote = new Quotation(user, user, asset);
    quote.addProduct(service);
    quote.setUserData(new UserData(1, 4, 10000));
    Subscription subs = new Subscription(quote);
    new SubscriptionJobScheduler().schedule(
        subs,
        service,
        new LinkedList<AbstractJob>(),
        12,
        4);
  }

}
