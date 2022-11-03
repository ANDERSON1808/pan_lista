import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPanListaAsociados, PanListaAsociados } from '../pan-lista-asociados.model';

import { PanListaAsociadosService } from './pan-lista-asociados.service';

describe('Service Tests', () => {
  describe('PanListaAsociados Service', () => {
    let service: PanListaAsociadosService;
    let httpMock: HttpTestingController;
    let elemDefault: IPanListaAsociados;
    let expectedResult: IPanListaAsociados | IPanListaAsociados[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PanListaAsociadosService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        listaAsociadosId: 0,
        grupo: 0,
        posicionId: 0,
        nombreCompleto: 'AAAAAAA',
        estadoHabil: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PanListaAsociados', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PanListaAsociados()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PanListaAsociados', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            listaAsociadosId: 1,
            grupo: 1,
            posicionId: 1,
            nombreCompleto: 'BBBBBB',
            estadoHabil: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PanListaAsociados', () => {
        const patchObject = Object.assign(
          {
            listaAsociadosId: 1,
            grupo: 1,
            estadoHabil: 'BBBBBB',
          },
          new PanListaAsociados()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PanListaAsociados', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            listaAsociadosId: 1,
            grupo: 1,
            posicionId: 1,
            nombreCompleto: 'BBBBBB',
            estadoHabil: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PanListaAsociados', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPanListaAsociadosToCollectionIfMissing', () => {
        it('should add a PanListaAsociados to an empty array', () => {
          const panListaAsociados: IPanListaAsociados = { id: 123 };
          expectedResult = service.addPanListaAsociadosToCollectionIfMissing([], panListaAsociados);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(panListaAsociados);
        });

        it('should not add a PanListaAsociados to an array that contains it', () => {
          const panListaAsociados: IPanListaAsociados = { id: 123 };
          const panListaAsociadosCollection: IPanListaAsociados[] = [
            {
              ...panListaAsociados,
            },
            { id: 456 },
          ];
          expectedResult = service.addPanListaAsociadosToCollectionIfMissing(panListaAsociadosCollection, panListaAsociados);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PanListaAsociados to an array that doesn't contain it", () => {
          const panListaAsociados: IPanListaAsociados = { id: 123 };
          const panListaAsociadosCollection: IPanListaAsociados[] = [{ id: 456 }];
          expectedResult = service.addPanListaAsociadosToCollectionIfMissing(panListaAsociadosCollection, panListaAsociados);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(panListaAsociados);
        });

        it('should add only unique PanListaAsociados to an array', () => {
          const panListaAsociadosArray: IPanListaAsociados[] = [{ id: 123 }, { id: 456 }, { id: 93962 }];
          const panListaAsociadosCollection: IPanListaAsociados[] = [{ id: 123 }];
          expectedResult = service.addPanListaAsociadosToCollectionIfMissing(panListaAsociadosCollection, ...panListaAsociadosArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const panListaAsociados: IPanListaAsociados = { id: 123 };
          const panListaAsociados2: IPanListaAsociados = { id: 456 };
          expectedResult = service.addPanListaAsociadosToCollectionIfMissing([], panListaAsociados, panListaAsociados2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(panListaAsociados);
          expect(expectedResult).toContain(panListaAsociados2);
        });

        it('should accept null and undefined values', () => {
          const panListaAsociados: IPanListaAsociados = { id: 123 };
          expectedResult = service.addPanListaAsociadosToCollectionIfMissing([], null, panListaAsociados, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(panListaAsociados);
        });

        it('should return initial array if no PanListaAsociados is added', () => {
          const panListaAsociadosCollection: IPanListaAsociados[] = [{ id: 123 }];
          expectedResult = service.addPanListaAsociadosToCollectionIfMissing(panListaAsociadosCollection, undefined, null);
          expect(expectedResult).toEqual(panListaAsociadosCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
