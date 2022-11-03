jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SorteosService } from '../service/sorteos.service';
import { ISorteos, Sorteos } from '../sorteos.model';

import { SorteosUpdateComponent } from './sorteos-update.component';

describe('Component Tests', () => {
  describe('Sorteos Management Update Component', () => {
    let comp: SorteosUpdateComponent;
    let fixture: ComponentFixture<SorteosUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sorteosService: SorteosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SorteosUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SorteosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SorteosUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sorteosService = TestBed.inject(SorteosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const sorteos: ISorteos = { id: 456 };

        activatedRoute.data = of({ sorteos });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(sorteos));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Sorteos>>();
        const sorteos = { id: 123 };
        jest.spyOn(sorteosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sorteos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sorteos }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sorteosService.update).toHaveBeenCalledWith(sorteos);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Sorteos>>();
        const sorteos = new Sorteos();
        jest.spyOn(sorteosService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sorteos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sorteos }));
        saveSubject.complete();

        // THEN
        expect(sorteosService.create).toHaveBeenCalledWith(sorteos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Sorteos>>();
        const sorteos = { id: 123 };
        jest.spyOn(sorteosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sorteos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sorteosService.update).toHaveBeenCalledWith(sorteos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
