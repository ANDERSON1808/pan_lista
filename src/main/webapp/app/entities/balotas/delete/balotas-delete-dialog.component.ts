import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBalotas } from '../balotas.model';
import { BalotasService } from '../service/balotas.service';

@Component({
  templateUrl: './balotas-delete-dialog.component.html',
})
export class BalotasDeleteDialogComponent {
  balotas?: IBalotas;

  constructor(protected balotasService: BalotasService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.balotasService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
