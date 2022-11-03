import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PanListaAsociadosService } from '../service/pan-lista-asociados.service';

import { PanListaAsociadosComponent } from './pan-lista-asociados.component';

describe('Component Tests', () => {
  describe('PanListaAsociados Management Component', () => {
    let comp: PanListaAsociadosComponent;
    let fixture: ComponentFixture<PanListaAsociadosComponent>;
    let service: PanListaAsociadosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PanListaAsociadosComponent],
      })
        .overrideTemplate(PanListaAsociadosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PanListaAsociadosComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PanListaAsociadosService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.panListaAsociados?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
