export interface IPanListaAsociados {
  id?: number;
  listaAsociadosId?: number | null;
  grupo?: number | null;
  posicionId?: number | null;
  nombreCompleto?: string | null;
  estadoHabil?: string | null;
}

export class PanListaAsociados implements IPanListaAsociados {
  constructor(
    public id?: number,
    public listaAsociadosId?: number | null,
    public grupo?: number | null,
    public posicionId?: number | null,
    public nombreCompleto?: string | null,
    public estadoHabil?: string | null
  ) {}
}

export function getPanListaAsociadosIdentifier(panListaAsociados: IPanListaAsociados): number | undefined {
  return panListaAsociados.id;
}
