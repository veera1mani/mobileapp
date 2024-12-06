import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OwnerOrdersPage } from './owner-orders.page';

describe('OwnerOrdersPage', () => {
  let component: OwnerOrdersPage;
  let fixture: ComponentFixture<OwnerOrdersPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(OwnerOrdersPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
