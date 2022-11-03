jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PanListaAsociadosService } from '../service/pan-lista-asociados.service';
import { IPanListaAsociados, PanListaAsociados } from '../pan-lista-asociados.model';

import { PanListaAsociadosUpdateComponent } from './pan-lista-asociados-update.component';

describe('Component Tests', () => {
  describe('PanListaAsociados Management Update Component', () => {
    let comp: PanListaAsociadosUpdateComponent;
    let fixture: ComponentFixture<PanListaAsociadosUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let panListaAsociadosService: PanListaAsociadosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PanListaAsociadosUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PanListaAsociadosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PanListaAsociadosUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      panListaAsociadosService = TestBed.inject(PanListaAsociadosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const panListaAsociados: IPanListaAsociados = { id: 456 };

        activatedRoute.data = of({ panListaAsociados });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(panListaAsociados));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PanListaAsociados>>();
        const panListaAsociados = { id: 123 };
        jest.spyOn(panListaAsociadosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ panListaAsociados });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: panListaAsociados }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(panListaAsociadosService.update).toHaveBeenCalledWith(panListaAsociados);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PanListaAsociados>>();
        const panListaAsociados = new PanListaAsociados();
        jest.spyOn(panListaAsociadosService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ panListaAsociados });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: panListaAsociados }));
        saveSubject.complete();

        // THEN
        expect(panListaAsociadosService.create).toHaveBeenCalledWith(panListaAsociados);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PanListaAsociados>>();
        const panListaAsociados = { id: 123 };
        jest.spyOn(panListaAsociadosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ panListaAsociados });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(panListaAsociadosService.update).toHaveBeenCalledWith(panListaAsociados);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
