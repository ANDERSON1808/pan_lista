import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISorteos, Sorteos } from '../sorteos.model';

import { SorteosService } from './sorteos.service';

describe('Service Tests', () => {
  describe('Sorteos Service', () => {
    let service: SorteosService;
    let httpMock: HttpTestingController;
    let elemDefault: ISorteos;
    let expectedResult: ISorteos | ISorteos[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SorteosService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        fechaSorteo: currentDate,
        codigo: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaSorteo: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Sorteos', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaSorteo: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaSorteo: currentDate,
          },
          returnedFromService
        );

        service.create(new Sorteos()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Sorteos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fechaSorteo: currentDate.format(DATE_TIME_FORMAT),
            codigo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaSorteo: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Sorteos', () => {
        const patchObject = Object.assign(
          {
            fechaSorteo: currentDate.format(DATE_TIME_FORMAT),
            codigo: 'BBBBBB',
          },
          new Sorteos()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaSorteo: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Sorteos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fechaSorteo: currentDate.format(DATE_TIME_FORMAT),
            codigo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaSorteo: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Sorteos', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSorteosToCollectionIfMissing', () => {
        it('should add a Sorteos to an empty array', () => {
          const sorteos: ISorteos = { id: 123 };
          expectedResult = service.addSorteosToCollectionIfMissing([], sorteos);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sorteos);
        });

        it('should not add a Sorteos to an array that contains it', () => {
          const sorteos: ISorteos = { id: 123 };
          const sorteosCollection: ISorteos[] = [
            {
              ...sorteos,
            },
            { id: 456 },
          ];
          expectedResult = service.addSorteosToCollectionIfMissing(sorteosCollection, sorteos);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Sorteos to an array that doesn't contain it", () => {
          const sorteos: ISorteos = { id: 123 };
          const sorteosCollection: ISorteos[] = [{ id: 456 }];
          expectedResult = service.addSorteosToCollectionIfMissing(sorteosCollection, sorteos);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sorteos);
        });

        it('should add only unique Sorteos to an array', () => {
          const sorteosArray: ISorteos[] = [{ id: 123 }, { id: 456 }, { id: 75902 }];
          const sorteosCollection: ISorteos[] = [{ id: 123 }];
          expectedResult = service.addSorteosToCollectionIfMissing(sorteosCollection, ...sorteosArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const sorteos: ISorteos = { id: 123 };
          const sorteos2: ISorteos = { id: 456 };
          expectedResult = service.addSorteosToCollectionIfMissing([], sorteos, sorteos2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sorteos);
          expect(expectedResult).toContain(sorteos2);
        });

        it('should accept null and undefined values', () => {
          const sorteos: ISorteos = { id: 123 };
          expectedResult = service.addSorteosToCollectionIfMissing([], null, sorteos, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sorteos);
        });

        it('should return initial array if no Sorteos is added', () => {
          const sorteosCollection: ISorteos[] = [{ id: 123 }];
          expectedResult = service.addSorteosToCollectionIfMissing(sorteosCollection, undefined, null);
          expect(expectedResult).toEqual(sorteosCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
