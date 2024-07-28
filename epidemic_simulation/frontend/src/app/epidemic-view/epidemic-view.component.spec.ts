import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EpidemicViewComponent } from './epidemic-view.component';

describe('EpidemicViewComponent', () => {
  let component: EpidemicViewComponent;
  let fixture: ComponentFixture<EpidemicViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EpidemicViewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EpidemicViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
