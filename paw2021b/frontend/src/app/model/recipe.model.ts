import {User} from "./user.model";


export class Recipe {
  constructor(
    public idRecipe: number,
    public author: User,
    public recipeTitle: string,
    public recipeDesc: string,
    public recipeSteps: string,
    public recipeDifficulty: number,
    public highlighted: boolean,
    public recipeTime: string,
    public recipeDateCreated: Date,
    public likesCount: number,
    public imageUrls: string[],
    public url: string,
    public commentsCount: number
  ) {
  }
}
