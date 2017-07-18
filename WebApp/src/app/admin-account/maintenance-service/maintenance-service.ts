export class MaintenanceService {

  public id: string;
  public name: string;
  public description: string;
  public applicableTo: string[];
  public amenities: string[];
  public category: string;
  public canSubscribe: boolean;
  public canRequestOneTime: boolean;
  public type: string;
  public active: boolean;
  public deleted: boolean;
  public subscriptionServiceData: any;
  public oneTimeServiceData: any;
  constructor() {}

}