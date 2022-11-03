jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISorteos, Sorteos } from '../sorteos.model';
import { SorteosService } from '../service/sorteos.service';

import { SorteosRoutingResolveService } from './sorteos-routing-resolve.service';

describe('Service Tests', () => {
  describe('Sorteos routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SorteosRoutingResolveService;
    let service: SorteosService;
    let resultSorteos: ISorteos | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SorteosRoutingResolveService);
      service = TestBed.inject(SorteosService);
      resultSorteos = undefined;
    });

    describe('resolve', () => {
      it('should return ISorteos returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSorteos = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSorteos).toEqual({ id: 123 });
      });

      it('should return new ISorteos if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSorteos = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSorteos).toEqual(new Sorteos());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Sorteos })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSorteos = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSorteos).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
