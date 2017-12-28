package com.subsede.amc.catalog.model;

import com.fasterxml.jackson.annotation.JsonValue;


public enum Category {

  ASSET {
    @JsonValue
    public ServiceType[] getServiceTypes() {
      return AssetServiceType.values();
    }

    @JsonValue
    public JobType getJobType() {
      return JobType.ASSET;
    }
  },
  UTILITY_BILL_PAYMENT {
    @JsonValue
    public ServiceType[] getServiceTypes() {
      return UtilityBillPaymentType.values();
    }
    @JsonValue
    public JobType getJobType() {
      return JobType.PAYMENT;
    }
  },
  LEGAL {
    @JsonValue
    public ServiceType[] getServiceTypes() {
      return LegalType.values();
    }

    @JsonValue
    public JobType getJobType() {
      return JobType.LEGAL;
    }
  };

  public abstract JobType getJobType();
  public abstract ServiceType[] getServiceTypes();


  public enum AssetServiceType implements ServiceType {
    ELECTRICAL {
      @JsonValue
      public String[] getSkills() {
        return new String[] { "ELECTRICAL" };
      }
    },
    PLUMBING {
      @JsonValue
      public String[] getSkills() {
        return new String[] { "PLUBMING", "SEWAGE" };
      }
    },

    LIFT_MAINTENANCE {
      @JsonValue
      public String[] getSkills() {
        return new String[] { "LIFT_MAINTENANCE" };
      }
    },
    SWIMMING_POOL {
      @JsonValue
      public String[] getSkills() {
        return new String[] { "SWIMMINGPOOL_CLEAN" };
      }
    },
    CLEANING {
      @JsonValue
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
    @JsonValue
    public String[] getSkills() {
      return new String[] { "MANUAL" };
    }

  }

  public enum LegalType implements ServiceType {

    DOCUMENT_VERIFICATION;

    public String getName() {
      return this.name();
    }
    @JsonValue
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
