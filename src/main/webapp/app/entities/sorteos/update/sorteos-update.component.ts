import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISorteos, Sorteos } from '../sorteos.model';
import { SorteosService } from '../service/sorteos.service';

@Component({
  selector: 'jhi-sorteos-update',
  templateUrl: './sorteos-update.component.html',
})
export class SorteosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fechaSorteo: [],
    codigo: [],
  });

  constructor(protected sorteosService: SorteosService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sorteos }) => {
      if (sorteos.id === undefined) {
        sorteos.fechaSorteo = dayjs().startOf('day');
      }

      this.updateForm(sorteos);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sorteos = this.createFromForm();
    if (sorteos.id !== undefined) {
      this.subscribeToSaveResponse(this.sorteosService.update(sorteos));
    } else {
      this.subscribeToSaveResponse(this.sorteosService.create(sorteos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISorteos>>): void {
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

  protected updateForm(sorteos: ISorteos): void {
    this.editForm.patchValue({
      id: sorteos.id,
      fechaSorteo: sorteos.fechaSorteo ? sorteos.fechaSorteo.format(DATE_TIME_FORMAT) : null,
      codigo: sorteos.codigo,
    });
  }

  protected createFromForm(): ISorteos {
    return {
      ...new Sorteos(),
      id: this.editForm.get(['id'])!.value,
      fechaSorteo: this.editForm.get(['fechaSorteo'])!.value
        ? dayjs(this.editForm.get(['fechaSorteo'])!.value, DATE_TIME_FORMAT)
        : undefined,
      codigo: this.editForm.get(['codigo'])!.value,
    };
  }
}
