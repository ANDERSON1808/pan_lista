import * as dayjs from 'dayjs';
import { ISorteos } from 'app/entities/sorteos/sorteos.model';
import { IPanListaAsociados } from 'app/entities/pan-lista-asociados/pan-lista-asociados.model';

export interface IBalotas {
  id?: number;
  numero?: number | null;
  creado?: dayjs.Dayjs | null;
  sorteos?: ISorteos | null;
  panListaAsociados?: IPanListaAsociados | null;
}

export class Balotas implements IBalotas {
  constructor(
    public id?: number,
    public numero?: number | null,
    public creado?: dayjs.Dayjs | null,
    public sorteos?: ISorteos | null,
    public panListaAsociados?: IPanListaAsociados | null
  ) {}
}

export function getBalotasIdentifier(balotas: IBalotas): number | undefined {
  return balotas.id;
}
