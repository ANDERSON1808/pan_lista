import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISorteos } from '../sorteos.model';

@Component({
  selector: 'jhi-sorteos-detail',
  templateUrl: './sorteos-detail.component.html',
})
export class SorteosDetailComponent implements OnInit {
  sorteos: ISorteos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sorteos }) => {
      this.sorteos = sorteos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
