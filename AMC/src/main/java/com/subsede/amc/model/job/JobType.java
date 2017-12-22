package com.subsede.amc.model.job;

import com.subsede.amc.catalog.model.Service;
import com.subsede.user.model.user.User;

public class JobType {

  public static AbstractJob getJob(Service service, JobMetadata jobMetadata, User customer) {
    switch (service.getCategory().getJobType()) {
    case ASSET:
      AssetMetadata4Job metadata = (AssetMetadata4Job) jobMetadata;
      return new AssetBasedJob(
          service.getName(),
          metadata.getAsset(),
          service.getServiceType(),
          metadata.getName(),
          metadata.getUniqueId());
    case LEGAL:
      LegalMetadata4Job legalData = (LegalMetadata4Job) jobMetadata;
      return new LegalJob(
          service.getName(),
          legalData.getDocuments(),
          service.getServiceType(),
          "LEGAL",
          "1",
          customer);
    case PAYMENT:
      return new BillPaymentJob(
          service.getName(),
          service.getServiceType(),
          "PAYMENT",
          "1",
          customer);
    default:
      return null;
    }
  }

}
