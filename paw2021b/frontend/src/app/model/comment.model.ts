import {User} from "./user.model";

export class Comment {
  constructor(
    public id: number,
    public recipeId: number,
    public commentDesc: String,
    public user: User,
    public dateCreated: Date
  ) {
  }
}
