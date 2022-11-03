jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPanListaAsociados, PanListaAsociados } from '../pan-lista-asociados.model';
import { PanListaAsociadosService } from '../service/pan-lista-asociados.service';

import { PanListaAsociadosRoutingResolveService } from './pan-lista-asociados-routing-resolve.service';

describe('Service Tests', () => {
  describe('PanListaAsociados routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PanListaAsociadosRoutingResolveService;
    let service: PanListaAsociadosService;
    let resultPanListaAsociados: IPanListaAsociados | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PanListaAsociadosRoutingResolveService);
      service = TestBed.inject(PanListaAsociadosService);
      resultPanListaAsociados = undefined;
    });

    describe('resolve', () => {
      it('should return IPanListaAsociados returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPanListaAsociados = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPanListaAsociados).toEqual({ id: 123 });
      });

      it('should return new IPanListaAsociados if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPanListaAsociados = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPanListaAsociados).toEqual(new PanListaAsociados());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PanListaAsociados })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPanListaAsociados = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPanListaAsociados).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
