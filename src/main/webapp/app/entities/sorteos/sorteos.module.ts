import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SorteosComponent } from './list/sorteos.component';
import { SorteosDetailComponent } from './detail/sorteos-detail.component';
import { SorteosUpdateComponent } from './update/sorteos-update.component';
import { SorteosDeleteDialogComponent } from './delete/sorteos-delete-dialog.component';
import { SorteosRoutingModule } from './route/sorteos-routing.module';

@NgModule({
  imports: [SharedModule, SorteosRoutingModule],
  declarations: [SorteosComponent, SorteosDetailComponent, SorteosUpdateComponent, SorteosDeleteDialogComponent],
  entryComponents: [SorteosDeleteDialogComponent],
})
export class SorteosModule {}
