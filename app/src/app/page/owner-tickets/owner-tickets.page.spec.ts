import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OwnerTicketsPage } from './owner-tickets.page';

describe('OwnerTicketsPage', () => {
  let component: OwnerTicketsPage;
  let fixture: ComponentFixture<OwnerTicketsPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(OwnerTicketsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
