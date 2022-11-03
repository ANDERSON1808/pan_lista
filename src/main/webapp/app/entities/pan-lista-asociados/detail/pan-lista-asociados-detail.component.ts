import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPanListaAsociados } from '../pan-lista-asociados.model';

@Component({
  selector: 'jhi-pan-lista-asociados-detail',
  templateUrl: './pan-lista-asociados-detail.component.html',
})
export class PanListaAsociadosDetailComponent implements OnInit {
  panListaAsociados: IPanListaAsociados | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ panListaAsociados }) => {
      this.panListaAsociados = panListaAsociados;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
