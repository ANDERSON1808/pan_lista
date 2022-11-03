import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BalotasDetailComponent } from './balotas-detail.component';

describe('Component Tests', () => {
  describe('Balotas Management Detail Component', () => {
    let comp: BalotasDetailComponent;
    let fixture: ComponentFixture<BalotasDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BalotasDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ balotas: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BalotasDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BalotasDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load balotas on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.balotas).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
