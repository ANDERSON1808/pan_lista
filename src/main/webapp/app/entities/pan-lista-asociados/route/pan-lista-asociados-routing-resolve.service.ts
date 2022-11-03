import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPanListaAsociados, PanListaAsociados } from '../pan-lista-asociados.model';
import { PanListaAsociadosService } from '../service/pan-lista-asociados.service';

@Injectable({ providedIn: 'root' })
export class PanListaAsociadosRoutingResolveService implements Resolve<IPanListaAsociados> {
  constructor(protected service: PanListaAsociadosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPanListaAsociados> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((panListaAsociados: HttpResponse<PanListaAsociados>) => {
          if (panListaAsociados.body) {
            return of(panListaAsociados.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PanListaAsociados());
  }
}
