import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserOrderPage } from './user-order.page';

describe('UserOrderPage', () => {
  let component: UserOrderPage;
  let fixture: ComponentFixture<UserOrderPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(UserOrderPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
