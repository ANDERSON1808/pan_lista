import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBalotas } from '../balotas.model';

@Component({
  selector: 'jhi-balotas-detail',
  templateUrl: './balotas-detail.component.html',
})
export class BalotasDetailComponent implements OnInit {
  balotas: IBalotas | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ balotas }) => {
      this.balotas = balotas;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
