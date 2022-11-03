import * as dayjs from 'dayjs';

export interface ISorteos {
  id?: number;
  fechaSorteo?: dayjs.Dayjs | null;
  codigo?: string | null;
}

export class Sorteos implements ISorteos {
  constructor(public id?: number, public fechaSorteo?: dayjs.Dayjs | null, public codigo?: string | null) {}
}

export function getSorteosIdentifier(sorteos: ISorteos): number | undefined {
  return sorteos.id;
}
