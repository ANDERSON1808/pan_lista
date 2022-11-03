import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBalotas, getBalotasIdentifier } from '../balotas.model';

export type EntityResponseType = HttpResponse<IBalotas>;
export type EntityArrayResponseType = HttpResponse<IBalotas[]>;

@Injectable({ providedIn: 'root' })
export class BalotasService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/balotas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(balotas: IBalotas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(balotas);
    return this.http
      .post<IBalotas>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(balotas: IBalotas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(balotas);
    return this.http
      .put<IBalotas>(`${this.resourceUrl}/${getBalotasIdentifier(balotas) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(balotas: IBalotas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(balotas);
    return this.http
      .patch<IBalotas>(`${this.resourceUrl}/${getBalotasIdentifier(balotas) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBalotas>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBalotas[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBalotasToCollectionIfMissing(balotasCollection: IBalotas[], ...balotasToCheck: (IBalotas | null | undefined)[]): IBalotas[] {
    const balotas: IBalotas[] = balotasToCheck.filter(isPresent);
    if (balotas.length > 0) {
      const balotasCollectionIdentifiers = balotasCollection.map(balotasItem => getBalotasIdentifier(balotasItem)!);
      const balotasToAdd = balotas.filter(balotasItem => {
        const balotasIdentifier = getBalotasIdentifier(balotasItem);
        if (balotasIdentifier == null || balotasCollectionIdentifiers.includes(balotasIdentifier)) {
          return false;
        }
        balotasCollectionIdentifiers.push(balotasIdentifier);
        return true;
      });
      return [...balotasToAdd, ...balotasCollection];
    }
    return balotasCollection;
  }

  protected convertDateFromClient(balotas: IBalotas): IBalotas {
    return Object.assign({}, balotas, {
      creado: balotas.creado?.isValid() ? balotas.creado.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creado = res.body.creado ? dayjs(res.body.creado) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((balotas: IBalotas) => {
        balotas.creado = balotas.creado ? dayjs(balotas.creado) : undefined;
      });
    }
    return res;
  }
}
