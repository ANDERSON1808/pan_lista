import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBalotas, Balotas } from '../balotas.model';

import { BalotasService } from './balotas.service';

describe('Service Tests', () => {
  describe('Balotas Service', () => {
    let service: BalotasService;
    let httpMock: HttpTestingController;
    let elemDefault: IBalotas;
    let expectedResult: IBalotas | IBalotas[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BalotasService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        numero: 0,
        creado: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            creado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Balotas', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            creado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creado: currentDate,
          },
          returnedFromService
        );

        service.create(new Balotas()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Balotas', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            numero: 1,
            creado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creado: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Balotas', () => {
        const patchObject = Object.assign(
          {
            numero: 1,
            creado: currentDate.format(DATE_TIME_FORMAT),
          },
          new Balotas()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            creado: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Balotas', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            numero: 1,
            creado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creado: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Balotas', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBalotasToCollectionIfMissing', () => {
        it('should add a Balotas to an empty array', () => {
          const balotas: IBalotas = { id: 123 };
          expectedResult = service.addBalotasToCollectionIfMissing([], balotas);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(balotas);
        });

        it('should not add a Balotas to an array that contains it', () => {
          const balotas: IBalotas = { id: 123 };
          const balotasCollection: IBalotas[] = [
            {
              ...balotas,
            },
            { id: 456 },
          ];
          expectedResult = service.addBalotasToCollectionIfMissing(balotasCollection, balotas);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Balotas to an array that doesn't contain it", () => {
          const balotas: IBalotas = { id: 123 };
          const balotasCollection: IBalotas[] = [{ id: 456 }];
          expectedResult = service.addBalotasToCollectionIfMissing(balotasCollection, balotas);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(balotas);
        });

        it('should add only unique Balotas to an array', () => {
          const balotasArray: IBalotas[] = [{ id: 123 }, { id: 456 }, { id: 82879 }];
          const balotasCollection: IBalotas[] = [{ id: 123 }];
          expectedResult = service.addBalotasToCollectionIfMissing(balotasCollection, ...balotasArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const balotas: IBalotas = { id: 123 };
          const balotas2: IBalotas = { id: 456 };
          expectedResult = service.addBalotasToCollectionIfMissing([], balotas, balotas2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(balotas);
          expect(expectedResult).toContain(balotas2);
        });

        it('should accept null and undefined values', () => {
          const balotas: IBalotas = { id: 123 };
          expectedResult = service.addBalotasToCollectionIfMissing([], null, balotas, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(balotas);
        });

        it('should return initial array if no Balotas is added', () => {
          const balotasCollection: IBalotas[] = [{ id: 123 }];
          expectedResult = service.addBalotasToCollectionIfMissing(balotasCollection, undefined, null);
          expect(expectedResult).toEqual(balotasCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
