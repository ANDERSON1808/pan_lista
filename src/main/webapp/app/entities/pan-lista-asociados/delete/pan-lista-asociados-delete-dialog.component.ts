import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPanListaAsociados } from '../pan-lista-asociados.model';
import { PanListaAsociadosService } from '../service/pan-lista-asociados.service';

@Component({
  templateUrl: './pan-lista-asociados-delete-dialog.component.html',
})
export class PanListaAsociadosDeleteDialogComponent {
  panListaAsociados?: IPanListaAsociados;

  constructor(protected panListaAsociadosService: PanListaAsociadosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.panListaAsociadosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
