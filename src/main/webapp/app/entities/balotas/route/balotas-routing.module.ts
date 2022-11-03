import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BalotasComponent } from '../list/balotas.component';
import { BalotasDetailComponent } from '../detail/balotas-detail.component';
import { BalotasUpdateComponent } from '../update/balotas-update.component';
import { BalotasRoutingResolveService } from './balotas-routing-resolve.service';

const balotasRoute: Routes = [
  {
    path: '',
    component: BalotasComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BalotasDetailComponent,
    resolve: {
      balotas: BalotasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BalotasUpdateComponent,
    resolve: {
      balotas: BalotasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BalotasUpdateComponent,
    resolve: {
      balotas: BalotasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(balotasRoute)],
  exports: [RouterModule],
})
export class BalotasRoutingModule {}
