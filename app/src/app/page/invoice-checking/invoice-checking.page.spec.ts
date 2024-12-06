import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InvoiceCheckingPage } from './invoice-checking.page';

describe('InvoiceCheckingPage', () => {
  let component: InvoiceCheckingPage;
  let fixture: ComponentFixture<InvoiceCheckingPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(InvoiceCheckingPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
