import { MaintenanceService } from './maintenance-service';
export const MAINTENANCESERVICES: MaintenanceService[] = [{
  "id": "5949434e2aaa132ca974ec78",
  "category": "ASSET",
  "name": "PLUMBING SERVICE",
  "description": "The plumbing service takes care of plumbing needs.",
  "canSubscribe": true,
  "canRequestOneTime": true,
  "applicableTo": [
    "APARTMENT",
    "FLAT",
    "HOUSE"
  ],
  "amenities": [
    "Sewage"
  ],
  "subscriptionServiceData": {
    "subscriptionData": [
      {
        "name": "FIVE",
        "visitCount": 10,
        "subscriptionPrice": 180,
        "discountPct": 0,
        "discount": 0
      },
      {
        "name": "THREE",
        "visitCount": 6,
        "subscriptionPrice": 140,
        "discountPct": 0,
        "discount": 0
      },
      {
        "name": "FOUR",
        "visitCount": 8,
        "subscriptionPrice": 160,
        "discountPct": 0,
        "discount": 0
      },
      {
        "name": "TWO",
        "visitCount": 4,
        "subscriptionPrice": 120,
        "discountPct": 0,
        "discount": 0
      },
      {
        "name": "ONE",
        "visitCount": 3,
        "subscriptionPrice": 100,
        "discountPct": 0,
        "discount": 0
      }
    ]
  },
  "oneTimeServiceData": {
    "oneTimeData": [
      {
        "name": "FIVE",
        "price": 18
      },
      {
        "name": "THREE",
        "price": 14
      },
      {
        "name": "FOUR",
        "price": 16
      },
      {
        "name": "TWO",
        "price": 12
      },
      {
        "name": "ONE",
        "price": 10
      }
    ]
  },
  "type": "SERVICE",
  "active": false,
  "deleted": false
}];