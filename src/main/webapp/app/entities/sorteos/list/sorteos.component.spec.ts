import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SorteosService } from '../service/sorteos.service';

import { SorteosComponent } from './sorteos.component';

describe('Component Tests', () => {
  describe('Sorteos Management Component', () => {
    let comp: SorteosComponent;
    let fixture: ComponentFixture<SorteosComponent>;
    let service: SorteosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SorteosComponent],
      })
        .overrideTemplate(SorteosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SorteosComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SorteosService);

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
      expect(comp.sorteos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
