import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChequeDetailsPage } from './cheque-details.page';

describe('ChequeDetailsPage', () => {
  let component: ChequeDetailsPage;
  let fixture: ComponentFixture<ChequeDetailsPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ChequeDetailsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
