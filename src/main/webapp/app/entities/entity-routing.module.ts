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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
