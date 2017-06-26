export class Paginator {

  public totalPages: number = 0;
  public totalElements: number = 0;
  public first: boolean = true;
  public last: boolean = true;
  public size: number = 20;
  public currentPage: number = 0;
  public numberOfElements: number = 0;
  public entity: string = "items";
  constructor(){}

}