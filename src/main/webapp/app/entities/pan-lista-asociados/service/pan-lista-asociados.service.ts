import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPanListaAsociados, getPanListaAsociadosIdentifier } from '../pan-lista-asociados.model';

export type EntityResponseType = HttpResponse<IPanListaAsociados>;
export type EntityArrayResponseType = HttpResponse<IPanListaAsociados[]>;

@Injectable({ providedIn: 'root' })
export class PanListaAsociadosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pan-lista-asociados');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(panListaAsociados: IPanListaAsociados): Observable<EntityResponseType> {
    return this.http.post<IPanListaAsociados>(this.resourceUrl, panListaAsociados, { observe: 'response' });
  }

  update(panListaAsociados: IPanListaAsociados): Observable<EntityResponseType> {
    return this.http.put<IPanListaAsociados>(
      `${this.resourceUrl}/${getPanListaAsociadosIdentifier(panListaAsociados) as number}`,
      panListaAsociados,
      { observe: 'response' }
    );
  }

  partialUpdate(panListaAsociados: IPanListaAsociados): Observable<EntityResponseType> {
    return this.http.patch<IPanListaAsociados>(
      `${this.resourceUrl}/${getPanListaAsociadosIdentifier(panListaAsociados) as number}`,
      panListaAsociados,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPanListaAsociados>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPanListaAsociados[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPanListaAsociadosToCollectionIfMissing(
    panListaAsociadosCollection: IPanListaAsociados[],
    ...panListaAsociadosToCheck: (IPanListaAsociados | null | undefined)[]
  ): IPanListaAsociados[] {
    const panListaAsociados: IPanListaAsociados[] = panListaAsociadosToCheck.filter(isPresent);
    if (panListaAsociados.length > 0) {
      const panListaAsociadosCollectionIdentifiers = panListaAsociadosCollection.map(
        panListaAsociadosItem => getPanListaAsociadosIdentifier(panListaAsociadosItem)!
      );
      const panListaAsociadosToAdd = panListaAsociados.filter(panListaAsociadosItem => {
        const panListaAsociadosIdentifier = getPanListaAsociadosIdentifier(panListaAsociadosItem);
        if (panListaAsociadosIdentifier == null || panListaAsociadosCollectionIdentifiers.includes(panListaAsociadosIdentifier)) {
          return false;
        }
        panListaAsociadosCollectionIdentifiers.push(panListaAsociadosIdentifier);
        return true;
      });
      return [...panListaAsociadosToAdd, ...panListaAsociadosCollection];
    }
    return panListaAsociadosCollection;
  }
}
