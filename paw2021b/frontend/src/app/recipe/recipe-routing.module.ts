import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {RecipeComponent} from "./recipe.component";
import {NewRecipeComponent} from "./new-recipe/new-recipe.component"
import {NotVerifiedGuard} from "../authentication/guards/not-verified.guard";
import {BannedGuardGuard} from "../authentication/guards/banned-guard.guard";

const routes: Routes = [
    {
        path: "new",
        component: NewRecipeComponent,
        canActivate: [NotVerifiedGuard, BannedGuardGuard]
    },
    {
        path: ':id',
        component: RecipeComponent,
        canActivate: [BannedGuardGuard]
    }
]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class RecipeRoutingModule {

}
