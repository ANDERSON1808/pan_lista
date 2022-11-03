import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PanListaAsociadosComponent } from '../list/pan-lista-asociados.component';
import { PanListaAsociadosDetailComponent } from '../detail/pan-lista-asociados-detail.component';
import { PanListaAsociadosUpdateComponent } from '../update/pan-lista-asociados-update.component';
import { PanListaAsociadosRoutingResolveService } from './pan-lista-asociados-routing-resolve.service';

const panListaAsociadosRoute: Routes = [
  {
    path: '',
    component: PanListaAsociadosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PanListaAsociadosDetailComponent,
    resolve: {
      panListaAsociados: PanListaAsociadosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PanListaAsociadosUpdateComponent,
    resolve: {
      panListaAsociados: PanListaAsociadosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PanListaAsociadosUpdateComponent,
    resolve: {
      panListaAsociados: PanListaAsociadosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(panListaAsociadosRoute)],
  exports: [RouterModule],
})
export class PanListaAsociadosRoutingModule {}
