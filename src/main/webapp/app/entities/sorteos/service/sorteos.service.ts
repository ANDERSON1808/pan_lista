import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISorteos, getSorteosIdentifier } from '../sorteos.model';

export type EntityResponseType = HttpResponse<ISorteos>;
export type EntityArrayResponseType = HttpResponse<ISorteos[]>;

@Injectable({ providedIn: 'root' })
export class SorteosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sorteos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sorteos: ISorteos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sorteos);
    return this.http
      .post<ISorteos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sorteos: ISorteos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sorteos);
    return this.http
      .put<ISorteos>(`${this.resourceUrl}/${getSorteosIdentifier(sorteos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sorteos: ISorteos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sorteos);
    return this.http
      .patch<ISorteos>(`${this.resourceUrl}/${getSorteosIdentifier(sorteos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISorteos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISorteos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSorteosToCollectionIfMissing(sorteosCollection: ISorteos[], ...sorteosToCheck: (ISorteos | null | undefined)[]): ISorteos[] {
    const sorteos: ISorteos[] = sorteosToCheck.filter(isPresent);
    if (sorteos.length > 0) {
      const sorteosCollectionIdentifiers = sorteosCollection.map(sorteosItem => getSorteosIdentifier(sorteosItem)!);
      const sorteosToAdd = sorteos.filter(sorteosItem => {
        const sorteosIdentifier = getSorteosIdentifier(sorteosItem);
        if (sorteosIdentifier == null || sorteosCollectionIdentifiers.includes(sorteosIdentifier)) {
          return false;
        }
        sorteosCollectionIdentifiers.push(sorteosIdentifier);
        return true;
      });
      return [...sorteosToAdd, ...sorteosCollection];
    }
    return sorteosCollection;
  }

  protected convertDateFromClient(sorteos: ISorteos): ISorteos {
    return Object.assign({}, sorteos, {
      fechaSorteo: sorteos.fechaSorteo?.isValid() ? sorteos.fechaSorteo.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaSorteo = res.body.fechaSorteo ? dayjs(res.body.fechaSorteo) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((sorteos: ISorteos) => {
        sorteos.fechaSorteo = sorteos.fechaSorteo ? dayjs(sorteos.fechaSorteo) : undefined;
      });
    }
    return res;
  }
}
