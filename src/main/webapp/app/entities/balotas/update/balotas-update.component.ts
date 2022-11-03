import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { Balotas, IBalotas } from '../balotas.model';
import { BalotasService } from '../service/balotas.service';
import { ISorteos } from 'app/entities/sorteos/sorteos.model';
import { SorteosService } from 'app/entities/sorteos/service/sorteos.service';
import { IPanListaAsociados } from 'app/entities/pan-lista-asociados/pan-lista-asociados.model';
import { PanListaAsociadosService } from 'app/entities/pan-lista-asociados/service/pan-lista-asociados.service';

@Component({
  selector: 'jhi-balotas-update',
  templateUrl: './balotas-update.component.html',
})
export class BalotasUpdateComponent implements OnInit {
  isSaving = false;

  sorteosSharedCollection: ISorteos[] = [];
  panListaAsociadosSharedCollection: IPanListaAsociados[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    creado: [],
    sorteos: [],
    panListaAsociados: [],
  });

  constructor(
    protected balotasService: BalotasService,
    protected sorteosService: SorteosService,
    protected panListaAsociadosService: PanListaAsociadosService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ balotas }) => {
      if (balotas.id === undefined) {
        balotas.creado = dayjs().startOf('day');
      }

      this.updateForm(balotas);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const balotas = this.createFromForm();
    if (balotas.id !== undefined) {
      this.subscribeToSaveResponse(this.balotasService.update(balotas));
    } else {
      this.subscribeToSaveResponse(this.balotasService.create(balotas));
    }
  }

  trackSorteosById(index: number, item: ISorteos): number {
    return item.id!;
  }

  trackPanListaAsociadosById(index: number, item: IPanListaAsociados): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBalotas>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(balotas: IBalotas): void {
    this.editForm.patchValue({
      id: balotas.id,
      numero: balotas.numero,
      creado: balotas.creado ? balotas.creado.format(DATE_TIME_FORMAT) : null,
      sorteos: balotas.sorteos,
      panListaAsociados: balotas.panListaAsociados,
    });

    this.sorteosSharedCollection = this.sorteosService.addSorteosToCollectionIfMissing(this.sorteosSharedCollection, balotas.sorteos);
    this.panListaAsociadosSharedCollection = this.panListaAsociadosService.addPanListaAsociadosToCollectionIfMissing(
      this.panListaAsociadosSharedCollection,
      balotas.panListaAsociados
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sorteosService
      .query()
      .pipe(map((res: HttpResponse<ISorteos[]>) => res.body ?? []))
      .pipe(map((sorteos: ISorteos[]) => this.sorteosService.addSorteosToCollectionIfMissing(sorteos, this.editForm.get('sorteos')!.value)))
      .subscribe((sorteos: ISorteos[]) => (this.sorteosSharedCollection = sorteos));

    this.panListaAsociadosService
      .query()
      .pipe(map((res: HttpResponse<IPanListaAsociados[]>) => res.body ?? []))
      .pipe(
        map((panListaAsociados: IPanListaAsociados[]) =>
          this.panListaAsociadosService.addPanListaAsociadosToCollectionIfMissing(
            panListaAsociados,
            this.editForm.get('panListaAsociados')!.value
          )
        )
      )
      .subscribe((panListaAsociados: IPanListaAsociados[]) => (this.panListaAsociadosSharedCollection = panListaAsociados));
  }

  protected createFromForm(): IBalotas {
    return {
      ...new Balotas(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      creado: this.editForm.get(['creado'])!.value ? dayjs(this.editForm.get(['creado'])!.value, DATE_TIME_FORMAT) : undefined,
      sorteos: this.editForm.get(['sorteos'])!.value,
      panListaAsociados: this.editForm.get(['panListaAsociados'])!.value,
    };
  }
}
