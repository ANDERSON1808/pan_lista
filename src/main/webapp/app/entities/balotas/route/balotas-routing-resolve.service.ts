import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBalotas, Balotas } from '../balotas.model';
import { BalotasService } from '../service/balotas.service';

@Injectable({ providedIn: 'root' })
export class BalotasRoutingResolveService implements Resolve<IBalotas> {
  constructor(protected service: BalotasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBalotas> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((balotas: HttpResponse<Balotas>) => {
          if (balotas.body) {
            return of(balotas.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Balotas());
  }
}
