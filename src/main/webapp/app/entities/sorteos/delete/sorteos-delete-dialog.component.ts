import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISorteos } from '../sorteos.model';
import { SorteosService } from '../service/sorteos.service';

@Component({
  templateUrl: './sorteos-delete-dialog.component.html',
})
export class SorteosDeleteDialogComponent {
  sorteos?: ISorteos;

  constructor(protected sorteosService: SorteosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sorteosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
