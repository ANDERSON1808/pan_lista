import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PanListaAsociadosDetailComponent } from './pan-lista-asociados-detail.component';

describe('Component Tests', () => {
  describe('PanListaAsociados Management Detail Component', () => {
    let comp: PanListaAsociadosDetailComponent;
    let fixture: ComponentFixture<PanListaAsociadosDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PanListaAsociadosDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ panListaAsociados: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PanListaAsociadosDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PanListaAsociadosDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load panListaAsociados on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.panListaAsociados).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
