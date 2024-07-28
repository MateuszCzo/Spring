import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EpidemicGraphComponent } from './epidemic-graph.component';

describe('EpidemicGraphComponent', () => {
  let component: EpidemicGraphComponent;
  let fixture: ComponentFixture<EpidemicGraphComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EpidemicGraphComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EpidemicGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
