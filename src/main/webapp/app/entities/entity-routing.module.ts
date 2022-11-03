import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pan-lista-asociados',
        data: { pageTitle: 'PanListaAsociados' },
        loadChildren: () => import('./pan-lista-asociados/pan-lista-asociados.module').then(m => m.PanListaAsociadosModule),
      },
      {
        path: 'sorteos',
        data: { pageTitle: 'Sorteos' },
        loadChildren: () => import('./sorteos/sorteos.module').then(m => m.SorteosModule),
      },
      {
        path: 'balotas',
        data: { pageTitle: 'Balotas' },
        loadChildren: () => import('./balotas/balotas.module').then(m => m.BalotasModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
