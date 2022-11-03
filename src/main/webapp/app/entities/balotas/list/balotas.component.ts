import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBalotas } from '../balotas.model';
import { BalotasService } from '../service/balotas.service';
import { BalotasDeleteDialogComponent } from '../delete/balotas-delete-dialog.component';

@Component({
  selector: 'jhi-balotas',
  templateUrl: './balotas.component.html',
})
export class BalotasComponent implements OnInit {
  balotas?: IBalotas[];
  isLoading = false;

  constructor(protected balotasService: BalotasService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.balotasService.query().subscribe(
      (res: HttpResponse<IBalotas[]>) => {
        this.isLoading = false;
        this.balotas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBalotas): number {
    return item.id!;
  }

  delete(balotas: IBalotas): void {
    const modalRef = this.modalService.open(BalotasDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.balotas = balotas;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
