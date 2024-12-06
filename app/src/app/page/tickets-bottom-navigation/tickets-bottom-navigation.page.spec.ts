import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TicketsBottomNavigationPage } from './tickets-bottom-navigation.page';

describe('TicketsBottomNavigationPage', () => {
  let component: TicketsBottomNavigationPage;
  let fixture: ComponentFixture<TicketsBottomNavigationPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(TicketsBottomNavigationPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
