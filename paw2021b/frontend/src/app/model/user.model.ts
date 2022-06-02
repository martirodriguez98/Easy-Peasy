export class User {

  constructor(
    public roles: string[],
    public id: number,
    public username: string,
    public email: string,
    public dateCreated: Date,
    public avatarUrl: string,
    public recipesCount: number,
    public highlightedCount: number
  ) {
  }
}
