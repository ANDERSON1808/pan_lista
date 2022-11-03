import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SorteosComponent } from '../list/sorteos.component';
import { SorteosDetailComponent } from '../detail/sorteos-detail.component';
import { SorteosUpdateComponent } from '../update/sorteos-update.component';
import { SorteosRoutingResolveService } from './sorteos-routing-resolve.service';

const sorteosRoute: Routes = [
  {
    path: '',
    component: SorteosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SorteosDetailComponent,
    resolve: {
      sorteos: SorteosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SorteosUpdateComponent,
    resolve: {
      sorteos: SorteosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SorteosUpdateComponent,
    resolve: {
      sorteos: SorteosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sorteosRoute)],
  exports: [RouterModule],
})
export class SorteosRoutingModule {}
