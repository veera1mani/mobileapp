import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OrdersBottomNavigationPage } from './orders-bottom-navigation.page';

describe('OrdersBottomNavigationPage', () => {
  let component: OrdersBottomNavigationPage;
  let fixture: ComponentFixture<OrdersBottomNavigationPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(OrdersBottomNavigationPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
