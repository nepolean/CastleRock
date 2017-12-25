package com.subsede.amc.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.subsede.amc.catalog.model.INonSubscriptionPackage;
import com.subsede.amc.catalog.model.Service;
import com.subsede.amc.catalog.repository.PackageRepository;
import com.subsede.amc.model.Asset;
import com.subsede.amc.model.NonSubscriptionOrder;
import com.subsede.amc.model.job.AbstractJob;
import com.subsede.amc.model.job.AssetMetadata4Job;
import com.subsede.amc.model.job.JobType;
import com.subsede.amc.model.job.ServiceMetadata;
import com.subsede.amc.repository.AssetRepository;
import com.subsede.amc.repository.NonSubscriptionOrderRepository;
import com.subsede.amc.service.job.JobScheduler;
import com.subsede.payment.model.PaymentRequest;
import com.subsede.payment.service.PaymentService;
import com.subsede.user.model.Customer;
import com.subsede.util.SecurityHelper;
import com.subsede.util.controller.exception.AuthorizationException;
import com.subsede.util.controller.exception.EntityNotFoundException;

@RestController
@RequestMapping(path = "/api/v1/order")
public class OneTimeOrderController {
  
  private static final Logger logger = LoggerFactory.getLogger(OneTimeOrderController.class);
  
  private AssetRepository assetRepository;
  private PackageRepository<INonSubscriptionPackage> pRepository;
  private NonSubscriptionOrderRepository nsoRepository;
  private SecurityHelper secHelper;

  private PaymentService paymentService;
  private JobScheduler jobScheduler;
  
  @Autowired
  public void setRepository(
      SecurityHelper secHelper,
      AssetRepository assetRepository,
      PackageRepository<INonSubscriptionPackage> pRepository,
      NonSubscriptionOrderRepository nsoRepository,
      PaymentService paymentService,
      JobScheduler jobScheduler) {
    this.secHelper = secHelper;
    this.assetRepository = assetRepository;
    this.pRepository = pRepository;
    this.nsoRepository = nsoRepository;
    this.paymentService = paymentService;
    this.jobScheduler = jobScheduler;
  }
  
  @PostMapping("/asset/{id}")
  public ResponseEntity<NonSubscriptionOrder> newOrder(@PathVariable String assetId, @Validated @RequestBody List<NonSubscriptionOrderDTO> packages, Principal principal) {
    logger.info("Requested for new order by ", principal.getName());
    // first create new order and then decide who made this request
    // validate packages for availability
    if (Objects.isNull(packages) || packages.isEmpty())
      throw new IllegalArgumentException("You should select at least one valid package/variant");
    Asset userAsset = ensureAssetMatches(assetId);
    ensureValidPackages(packages);    
    /* Now that validations are successful, process the order */
    NonSubscriptionOrder order = new NonSubscriptionOrder((Customer)secHelper.getLoggedInUser());
    for (NonSubscriptionOrderDTO selectedPkg : packages) {
      order.addPackage(selectedPkg.getPkg(), selectedPkg.getPkgVariant());
    }
    ServiceMetadata metadata = new AssetMetadata4Job(userAsset);
    order.setUserData(metadata);
    order.confirmOrder();
    NonSubscriptionOrder newOrder = this.nsoRepository.insert(order);
    return ResponseEntity.ok(newOrder);
  }

  @PostMapping("/{id}/confirm")
  public void confirmOrder(@PathVariable String orderId) {
    logger.info("Confirming Order with id {}", orderId);
    NonSubscriptionOrder order = findOrder(orderId);
    PaymentRequest payReq = new PaymentRequest();
    payReq.setAmount(order.getTotalAmount());
    String callbackURI = "/".concat(order.getId().concat("/payment/confirm"));
    payReq.setCallbackURI(callbackURI);
    PaymentRequest savedRequest = this.paymentService.newPaymentRequest(payReq);
    order.setPaymentId(savedRequest.getPaymentRequestId());
    this.nsoRepository.save(order);
  }
  
  @PostMapping("/{id}/payment/confirm")
  public void confirmPayment(@PathVariable String orderId) {
    logger.info("Payment confirmation for the order {}", orderId);
    NonSubscriptionOrder order = findOrder(orderId);
    order.paymentCompleted();
    this.nsoRepository.save(order);
    scheduleJob(order);
  }

  private void scheduleJob(NonSubscriptionOrder order) {
    if (logger.isDebugEnabled())
      logger.debug("scheduling jobs");
    Set<Service> services = order.getServices();
    Set<AbstractJob> jobs = Collections.emptySet();
    for (Service service:services) {
      jobs.add(JobType.getJob(service,  order.getUserData(), order.getCustomer()));
    }
    this.jobScheduler.scheduleImmediate(jobs);
  }

  private void ensureValidPackages(List<NonSubscriptionOrderDTO> packages) {
    Set<String> packageIds = Collections.emptySet();    
    for(NonSubscriptionOrderDTO selectedPkg : packages) {
      packageIds.add(selectedPkg.getPkgId());
    }    
    List<INonSubscriptionPackage> pkgList = this.pRepository.findValidProducts(packageIds);
    List<NonSubscriptionOrderDTO> invalidPackages = Collections.emptyList();
    for (NonSubscriptionOrderDTO selectedPkg : packages) {
      boolean valid = false;
      for(INonSubscriptionPackage pkg : pkgList) {
        if ((selectedPkg.getPkgId().equals(pkg.getId())) && pkg.getVariants().containsKey(selectedPkg.getVariant())) {
          valid = true;
          selectedPkg.setPkg(pkg);
          selectedPkg.setPkgVariant(pkg.getVariants().get(selectedPkg.getVariant()));
        }
      }
      if (!valid) {
        if (logger.isWarnEnabled())
          logger.warn("The packageid, {}, and variant, {}, is not valid", selectedPkg.getPkgId(), selectedPkg.getVariant());
        invalidPackages.add(selectedPkg);
      }
    }
    if (invalidPackages.size() > 0)
      throw new IllegalArgumentException(
          String.format("One or more selected package(s) [{}] is not valid", invalidPackages.toString()));
  }

  private Asset ensureAssetMatches(String assetId) {
    Asset asset = this.assetRepository.findOne(assetId);
    String username = this.secHelper.getLoggedInUsername();
    if (asset == null)
      throw new EntityNotFoundException(assetId, "Asset");
    if (!asset.getAssetOwner().contains(username))
      throw new AuthorizationException("Only owner should be able to request for a service");
    return asset; 
  }

  private NonSubscriptionOrder findOrder(String orderId) {
    NonSubscriptionOrder order = this.nsoRepository.findOne(orderId);
    if (order == null)
      throw new EntityNotFoundException(orderId, "NonSubscriptionOrder");
    return order;
  }
}
