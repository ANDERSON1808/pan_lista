import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PanListaAsociadosComponent } from './list/pan-lista-asociados.component';
import { PanListaAsociadosDetailComponent } from './detail/pan-lista-asociados-detail.component';
import { PanListaAsociadosUpdateComponent } from './update/pan-lista-asociados-update.component';
import { PanListaAsociadosDeleteDialogComponent } from './delete/pan-lista-asociados-delete-dialog.component';
import { PanListaAsociadosRoutingModule } from './route/pan-lista-asociados-routing.module';

@NgModule({
  imports: [SharedModule, PanListaAsociadosRoutingModule],
  declarations: [
    PanListaAsociadosComponent,
    PanListaAsociadosDetailComponent,
    PanListaAsociadosUpdateComponent,
    PanListaAsociadosDeleteDialogComponent,
  ],
  entryComponents: [PanListaAsociadosDeleteDialogComponent],
})
export class PanListaAsociadosModule {}
