import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChequesPage } from './cheques.page';

describe('ChequesPage', () => {
  let component: ChequesPage;
  let fixture: ComponentFixture<ChequesPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ChequesPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
