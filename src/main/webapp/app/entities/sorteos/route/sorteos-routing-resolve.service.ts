import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISorteos, Sorteos } from '../sorteos.model';
import { SorteosService } from '../service/sorteos.service';

@Injectable({ providedIn: 'root' })
export class SorteosRoutingResolveService implements Resolve<ISorteos> {
  constructor(protected service: SorteosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISorteos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sorteos: HttpResponse<Sorteos>) => {
          if (sorteos.body) {
            return of(sorteos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sorteos());
  }
}
