package com.subsede.amc.model.subscription;

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

import com.subsede.amc.catalog.model.AMCPackage;
import com.subsede.amc.catalog.model.Category;
import com.subsede.amc.catalog.model.ISubscriptionPackage;
import com.subsede.amc.catalog.model.Rating;
import com.subsede.amc.catalog.model.Service;
import com.subsede.amc.catalog.model.SubscriptionData;
import com.subsede.amc.catalog.model.asset.AssetBasedService;
import com.subsede.amc.catalog.model.asset.AssetType;
import com.subsede.amc.catalog.model.asset.RatingBasedSubscriptionData;
import com.subsede.amc.model.Asset;
import com.subsede.amc.model.Location;
import com.subsede.amc.model.UserData;
import com.subsede.amc.model.job.AbstractJob;
import com.subsede.amc.model.job.AssetMetadata4Job;
import com.subsede.amc.model.job.JobType;
import com.subsede.amc.model.job.ServiceMetadata;
import com.subsede.amc.model.quote.Quotation;
import com.subsede.amc.repository.JobRepository;
import com.subsede.user.model.Customer;

@Component
public class SubscriptionJobScheduler {

  private static Logger logger = LoggerFactory.getLogger(SubscriptionJobScheduler.class);
  private JobRepository jobRepository;

  @Autowired
  public void setJobRepository(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }
  
  public void scheduleImmediate(List<AbstractJob> jobs) {
    
  }
  
  public void scheduleFrequency(List<AbstractJob> jobs) {
    
  }

  public void scheduleJob(Subscription subscription) {
    logger.info("Schedule jobs for subscription with id {}", subscription.getId());
    Set<ISubscriptionPackage> services = subscription.getProducts();
    List<AbstractJob> jobs = new LinkedList<>();
    for (ISubscriptionPackage service : services) {
      SubscriptionData serviceData = service.fetchSubscriptionData(subscription.getUserData().getInput());
      int visitsPerQuarter = serviceData.getVisitCount();
      int noOfQuarters = subscription.getUserData().getTenure();
      int expected = visitsPerQuarter * noOfQuarters;
      schedule(subscription, (Service) service, jobs, visitsPerQuarter, noOfQuarters);
      if (expected != jobs.size()) {
        if (logger.isErrorEnabled())
          logger.error("Expected count {} and actual count", expected, jobs.size());
        throw new IllegalStateException("Something went wrong with the system.");
      }
    }
    this.jobRepository.save(jobs);
  }

  private void schedule(
      Subscription subscription,
      Service service,
      List<AbstractJob> jobs,
      int visitsPerQuarter,
      int noOfQuarters) {

    int noOfDays = 90; // approx per quarter
    int durationBetweenServices = noOfDays / visitsPerQuarter;
    int start = durationBetweenServices;
    for (int i = 0; i < noOfQuarters; i++) {
      for (int j = 0; j < visitsPerQuarter; j++) {
        Date after = getDateAfter(start);
        if (logger.isDebugEnabled())
          logger.debug("Job for service {} is scheduled on {}", service.getName(), after);
        ServiceMetadata metadata = new AssetMetadata4Job(
            subscription.getAsset(),
            subscription.getClass().getName(),
            subscription.getId());
        AbstractJob job = JobType.getJob(service, metadata, subscription.getOwner());
        if (job != null)
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

  public static void main1(String[] args) {
    System.setProperty("ENVIRONMENT", "TEST");
    Customer user = new Customer("First", "Last", "first@gmail.com");
    Asset asset = new Asset("",new Location(), AssetType.FLAT);
    Service service = new AssetBasedService("A", "ABC", null, null, null);
    Map<Rating, SubscriptionData> data = new HashMap<Rating, SubscriptionData>();
    for (Rating key : Rating.values())
      data.put(key, new SubscriptionData(100.0, 3));
    service.setSubscriptionData(new RatingBasedSubscriptionData(data));
    ISubscriptionPackage pkg = new AMCPackage(Category.ASSET, "Maintenance", "Complete maintenance");
    pkg.addService(service);
    Quotation quote = new Quotation(user, user, asset);
    quote.addProduct(pkg);
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
