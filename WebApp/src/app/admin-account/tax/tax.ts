export class Tax {

  public id: string;
  public type: string;
  public percentage: number;
  constructor(id, type, percentage) {
    this.id = id;
    this.type = type;
    this.percentage = percentage;
  }

}