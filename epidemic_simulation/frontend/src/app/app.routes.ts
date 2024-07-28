import { Routes } from '@angular/router';
import { EpidemicListComponent } from './epidemic-list/epidemic-list.component';
import { HomeComponent } from './home/home.component';
import { EpidemicViewComponent } from './epidemic-view/epidemic-view.component';
import { EpidemicEditComponent } from './epidemic-edit/epidemic-edit.component';
import { EpidemicCreateComponent } from './epidemic-create/epidemic-create.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'epidemic', component: EpidemicListComponent },
    { path: 'epidemic/create', component: EpidemicCreateComponent },
    { path: 'epidemic/view/:id', component: EpidemicViewComponent },
    { path: 'epidemic/edit/:id', component: EpidemicEditComponent },
];
