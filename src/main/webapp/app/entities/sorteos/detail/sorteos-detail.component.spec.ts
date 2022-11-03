import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SorteosDetailComponent } from './sorteos-detail.component';

describe('Component Tests', () => {
  describe('Sorteos Management Detail Component', () => {
    let comp: SorteosDetailComponent;
    let fixture: ComponentFixture<SorteosDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SorteosDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ sorteos: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SorteosDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SorteosDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sorteos on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sorteos).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
