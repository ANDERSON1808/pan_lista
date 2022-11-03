jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBalotas, Balotas } from '../balotas.model';
import { BalotasService } from '../service/balotas.service';

import { BalotasRoutingResolveService } from './balotas-routing-resolve.service';

describe('Service Tests', () => {
  describe('Balotas routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BalotasRoutingResolveService;
    let service: BalotasService;
    let resultBalotas: IBalotas | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BalotasRoutingResolveService);
      service = TestBed.inject(BalotasService);
      resultBalotas = undefined;
    });

    describe('resolve', () => {
      it('should return IBalotas returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBalotas = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBalotas).toEqual({ id: 123 });
      });

      it('should return new IBalotas if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBalotas = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBalotas).toEqual(new Balotas());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Balotas })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBalotas = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBalotas).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
