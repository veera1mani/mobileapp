import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OwnerDispatchPage } from './owner-dispatch.page';

describe('OwnerDispatchPage', () => {
  let component: OwnerDispatchPage;
  let fixture: ComponentFixture<OwnerDispatchPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(OwnerDispatchPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
