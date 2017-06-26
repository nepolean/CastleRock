export class User {

  public username: string;
  public email: string;
  public firstName: string;
  public lastName: string;
  public middleName: string;
  public roles?: any[];
  public enabled: boolean;
  public accountLocked: boolean;
  public accountExpired: boolean;
  public passwordExpired: boolean;
  constructor(){}

}