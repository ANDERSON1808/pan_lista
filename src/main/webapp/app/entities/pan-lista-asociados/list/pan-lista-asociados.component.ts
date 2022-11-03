import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPanListaAsociados } from '../pan-lista-asociados.model';
import { PanListaAsociadosService } from '../service/pan-lista-asociados.service';
import { PanListaAsociadosDeleteDialogComponent } from '../delete/pan-lista-asociados-delete-dialog.component';

@Component({
  selector: 'jhi-pan-lista-asociados',
  templateUrl: './pan-lista-asociados.component.html',
})
export class PanListaAsociadosComponent implements OnInit {
  panListaAsociados?: IPanListaAsociados[];
  isLoading = false;

  constructor(protected panListaAsociadosService: PanListaAsociadosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.panListaAsociadosService.query().subscribe(
      (res: HttpResponse<IPanListaAsociados[]>) => {
        this.isLoading = false;
        this.panListaAsociados = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPanListaAsociados): number {
    return item.id!;
  }

  delete(panListaAsociados: IPanListaAsociados): void {
    const modalRef = this.modalService.open(PanListaAsociadosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.panListaAsociados = panListaAsociados;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
