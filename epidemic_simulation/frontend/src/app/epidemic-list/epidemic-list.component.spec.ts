import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EpidemicListComponent } from './epidemic-list.component';

describe('EpidemicListComponent', () => {
  let component: EpidemicListComponent;
  let fixture: ComponentFixture<EpidemicListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EpidemicListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EpidemicListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
