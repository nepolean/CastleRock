package com.real.proj.amc.model;

public enum Category {

  ASSET {
    public ServiceType[] getServiceTypes() {
      return AssetServiceType.values();
    }

    public JobType getJobType() {
      return JobType.ASSET;
    }
  },
  UTILITY_BILL_PAYMENT {
    public ServiceType[] getServiceTypes() {
      return UtilityBillPaymentType.values();
    }

    public JobType getJobType() {
      return JobType.PAYMENT;
    }
  },
  LEGAL {
    public ServiceType[] getServiceTypes() {
      return LegalType.values();
    }

    public JobType getJobType() {
      return JobType.LEGAL;
    }
  };

  public abstract ServiceType[] getServiceTypes();

  public abstract JobType getJobType();

  public enum AssetServiceType implements ServiceType {
    ELECTRICAL {
      public String[] getSkills() {
        return new String[] { "ELECTRICAL" };
      }
    },
    PLUMBING {
      public String[] getSkills() {
        return new String[] { "PLUBMING", "SEWAGE" };
      }
    },

    LIFT_MAINTENANCE {
      public String[] getSkills() {
        return new String[] { "LIFT_MAINTENANCE" };
      }
    },
    SWIMMING_POOL {
      public String[] getSkills() {
        return new String[] { "SWIMMINGPOOL_CLEAN" };
      }
    },
    CLEANING {

      public String[] getSkills() {
        return new String[] { "GENERAL_PROPERTY_CLEAN" };
      }

    };

    public abstract String[] getSkills();

    public String getName() {
      return this.getName();
    }

  }

  public enum UtilityBillPaymentType implements ServiceType {

    ELECTRICITY,
    WATER,
    PROPERTYTAX;

    public String getName() {
      return this.name();
    }

    public String[] getSkills() {
      return new String[] { "BILLPAYMENT" };
    }

  }

  public enum LegalType implements ServiceType {

    DOCUMENT_VERIFICATION;

    public String getName() {
      return this.name();
    }

    public String[] getSkills() {
      return new String[] { "LEGAL" };
    }

  }

  public enum JobType {
    ASSET,
    LEGAL,
    PAYMENT
  }
}
