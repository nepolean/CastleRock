export class Profile {

  public username: string;
  public email: string;
  public firstName: string;
  public lastName: string;
  public middleName: string;
  constructor(username?: string) {
    this.username = username;
  }

}