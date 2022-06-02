import {User} from "./user.model";
import {Comment} from "./comment.model";

export class Report {
  constructor(
    public idReport: number,
    public reporter: User,
    public reported: User,
    public desc: string,
    public comment: Comment,
    public date: Date
  ) {
  }
}
