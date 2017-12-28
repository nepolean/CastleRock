package com.subsede.amc.model.job;

import com.subsede.amc.catalog.model.Service;
import com.subsede.user.model.user.User;

public class JobType {

  public static AbstractJob getJob(Service service, ServiceMetadata jobMetadata, User customer) {
    switch (service.getCategory().getJobType()) {
    case ASSET:
      AssetMetadata4Job metadata = (AssetMetadata4Job) jobMetadata;
      return new AssetBasedJob(
          service.getName(),
          metadata.getAsset(),
          service.getSkills(),
          metadata.getSource(),
          metadata.getUniqueId());
    case LEGAL:
      LegalMetadata4Job legalData = (LegalMetadata4Job) jobMetadata;
      return new LegalJob(
          service.getName(),
          legalData.getDocuments(),
          service.getSkills(),
          legalData.getSource(),
          legalData.getUniqueId(),
          customer);
    case PAYMENT:
      PaymentMetadata4Job pmtData = (PaymentMetadata4Job) jobMetadata;
      return new BillPaymentJob(
          service.getName(),
          service.getSkills(),
          pmtData.getSource(),
          pmtData.getUniqueId(),
          customer);
    default:
      return null;
    }
  }

}
