import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BalotasComponent } from './list/balotas.component';
import { BalotasDetailComponent } from './detail/balotas-detail.component';
import { BalotasUpdateComponent } from './update/balotas-update.component';
import { BalotasDeleteDialogComponent } from './delete/balotas-delete-dialog.component';
import { BalotasRoutingModule } from './route/balotas-routing.module';

@NgModule({
  imports: [SharedModule, BalotasRoutingModule],
  declarations: [BalotasComponent, BalotasDetailComponent, BalotasUpdateComponent, BalotasDeleteDialogComponent],
  entryComponents: [BalotasDeleteDialogComponent],
})
export class BalotasModule {}
