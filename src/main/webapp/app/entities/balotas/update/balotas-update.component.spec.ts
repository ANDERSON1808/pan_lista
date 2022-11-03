jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BalotasService } from '../service/balotas.service';
import { IBalotas, Balotas } from '../balotas.model';
import { ISorteos } from 'app/entities/sorteos/sorteos.model';
import { SorteosService } from 'app/entities/sorteos/service/sorteos.service';
import { IPanListaAsociados } from 'app/entities/pan-lista-asociados/pan-lista-asociados.model';
import { PanListaAsociadosService } from 'app/entities/pan-lista-asociados/service/pan-lista-asociados.service';

import { BalotasUpdateComponent } from './balotas-update.component';

describe('Component Tests', () => {
  describe('Balotas Management Update Component', () => {
    let comp: BalotasUpdateComponent;
    let fixture: ComponentFixture<BalotasUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let balotasService: BalotasService;
    let sorteosService: SorteosService;
    let panListaAsociadosService: PanListaAsociadosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BalotasUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BalotasUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BalotasUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      balotasService = TestBed.inject(BalotasService);
      sorteosService = TestBed.inject(SorteosService);
      panListaAsociadosService = TestBed.inject(PanListaAsociadosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Sorteos query and add missing value', () => {
        const balotas: IBalotas = { id: 456 };
        const sorteos: ISorteos = { id: 18999 };
        balotas.sorteos = sorteos;

        const sorteosCollection: ISorteos[] = [{ id: 3279 }];
        jest.spyOn(sorteosService, 'query').mockReturnValue(of(new HttpResponse({ body: sorteosCollection })));
        const additionalSorteos = [sorteos];
        const expectedCollection: ISorteos[] = [...additionalSorteos, ...sorteosCollection];
        jest.spyOn(sorteosService, 'addSorteosToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ balotas });
        comp.ngOnInit();

        expect(sorteosService.query).toHaveBeenCalled();
        expect(sorteosService.addSorteosToCollectionIfMissing).toHaveBeenCalledWith(sorteosCollection, ...additionalSorteos);
        expect(comp.sorteosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call PanListaAsociados query and add missing value', () => {
        const balotas: IBalotas = { id: 456 };
        const panListaAsociados: IPanListaAsociados = { id: 95860 };
        balotas.panListaAsociados = panListaAsociados;

        const panListaAsociadosCollection: IPanListaAsociados[] = [{ id: 40940 }];
        jest.spyOn(panListaAsociadosService, 'query').mockReturnValue(of(new HttpResponse({ body: panListaAsociadosCollection })));
        const additionalPanListaAsociados = [panListaAsociados];
        const expectedCollection: IPanListaAsociados[] = [...additionalPanListaAsociados, ...panListaAsociadosCollection];
        jest.spyOn(panListaAsociadosService, 'addPanListaAsociadosToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ balotas });
        comp.ngOnInit();

        expect(panListaAsociadosService.query).toHaveBeenCalled();
        expect(panListaAsociadosService.addPanListaAsociadosToCollectionIfMissing).toHaveBeenCalledWith(
          panListaAsociadosCollection,
          ...additionalPanListaAsociados
        );
        expect(comp.panListaAsociadosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const balotas: IBalotas = { id: 456 };
        const sorteos: ISorteos = { id: 16803 };
        balotas.sorteos = sorteos;
        const panListaAsociados: IPanListaAsociados = { id: 46549 };
        balotas.panListaAsociados = panListaAsociados;

        activatedRoute.data = of({ balotas });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(balotas));
        expect(comp.sorteosSharedCollection).toContain(sorteos);
        expect(comp.panListaAsociadosSharedCollection).toContain(panListaAsociados);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Balotas>>();
        const balotas = { id: 123 };
        jest.spyOn(balotasService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ balotas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: balotas }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(balotasService.update).toHaveBeenCalledWith(balotas);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Balotas>>();
        const balotas = new Balotas();
        jest.spyOn(balotasService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ balotas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: balotas }));
        saveSubject.complete();

        // THEN
        expect(balotasService.create).toHaveBeenCalledWith(balotas);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Balotas>>();
        const balotas = { id: 123 };
        jest.spyOn(balotasService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ balotas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(balotasService.update).toHaveBeenCalledWith(balotas);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSorteosById', () => {
        it('Should return tracked Sorteos primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSorteosById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPanListaAsociadosById', () => {
        it('Should return tracked PanListaAsociados primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPanListaAsociadosById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
