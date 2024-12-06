import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TransportOrderPage } from './transport-order.page';

describe('TransportOrderPage', () => {
  let component: TransportOrderPage;
  let fixture: ComponentFixture<TransportOrderPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(TransportOrderPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
