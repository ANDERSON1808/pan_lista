import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPanListaAsociados, PanListaAsociados } from '../pan-lista-asociados.model';
import { PanListaAsociadosService } from '../service/pan-lista-asociados.service';

@Component({
  selector: 'jhi-pan-lista-asociados-update',
  templateUrl: './pan-lista-asociados-update.component.html',
})
export class PanListaAsociadosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    listaAsociadosId: [],
    grupo: [],
    posicionId: [],
    nombreCompleto: [null, [Validators.maxLength(255)]],
    estadoHabil: [null, [Validators.maxLength(255)]],
  });

  constructor(
    protected panListaAsociadosService: PanListaAsociadosService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ panListaAsociados }) => {
      this.updateForm(panListaAsociados);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const panListaAsociados = this.createFromForm();
    if (panListaAsociados.id !== undefined) {
      this.subscribeToSaveResponse(this.panListaAsociadosService.update(panListaAsociados));
    } else {
      this.subscribeToSaveResponse(this.panListaAsociadosService.create(panListaAsociados));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPanListaAsociados>>): void {
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

  protected updateForm(panListaAsociados: IPanListaAsociados): void {
    this.editForm.patchValue({
      id: panListaAsociados.id,
      listaAsociadosId: panListaAsociados.listaAsociadosId,
      grupo: panListaAsociados.grupo,
      posicionId: panListaAsociados.posicionId,
      nombreCompleto: panListaAsociados.nombreCompleto,
      estadoHabil: panListaAsociados.estadoHabil,
    });
  }

  protected createFromForm(): IPanListaAsociados {
    return {
      ...new PanListaAsociados(),
      id: this.editForm.get(['id'])!.value,
      listaAsociadosId: this.editForm.get(['listaAsociadosId'])!.value,
      grupo: this.editForm.get(['grupo'])!.value,
      posicionId: this.editForm.get(['posicionId'])!.value,
      nombreCompleto: this.editForm.get(['nombreCompleto'])!.value,
      estadoHabil: this.editForm.get(['estadoHabil'])!.value,
    };
  }
}
