import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReturnsBottomNavigationPage } from './returns-bottom-navigation.page';

describe('ReturnsBottomNavigationPage', () => {
  let component: ReturnsBottomNavigationPage;
  let fixture: ComponentFixture<ReturnsBottomNavigationPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ReturnsBottomNavigationPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
