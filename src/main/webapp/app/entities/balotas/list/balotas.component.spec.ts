import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BalotasService } from '../service/balotas.service';

import { BalotasComponent } from './balotas.component';

describe('Component Tests', () => {
  describe('Balotas Management Component', () => {
    let comp: BalotasComponent;
    let fixture: ComponentFixture<BalotasComponent>;
    let service: BalotasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BalotasComponent],
      })
        .overrideTemplate(BalotasComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BalotasComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BalotasService);

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
      expect(comp.balotas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
