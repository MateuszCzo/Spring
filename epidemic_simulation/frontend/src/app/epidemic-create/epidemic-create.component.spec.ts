import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EpidemicCreateComponent } from './epidemic-create.component';

describe('EpidemicCreateComponent', () => {
  let component: EpidemicCreateComponent;
  let fixture: ComponentFixture<EpidemicCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EpidemicCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EpidemicCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
