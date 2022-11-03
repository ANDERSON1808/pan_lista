import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISorteos } from '../sorteos.model';
import { SorteosService } from '../service/sorteos.service';
import { SorteosDeleteDialogComponent } from '../delete/sorteos-delete-dialog.component';

@Component({
  selector: 'jhi-sorteos',
  templateUrl: './sorteos.component.html',
})
export class SorteosComponent implements OnInit {
  sorteos?: ISorteos[];
  isLoading = false;

  constructor(protected sorteosService: SorteosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sorteosService.query().subscribe(
      (res: HttpResponse<ISorteos[]>) => {
        this.isLoading = false;
        this.sorteos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISorteos): number {
    return item.id!;
  }

  delete(sorteos: ISorteos): void {
    const modalRef = this.modalService.open(SorteosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sorteos = sorteos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
