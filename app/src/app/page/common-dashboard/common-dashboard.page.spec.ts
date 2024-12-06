import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommonDashboardPage } from './common-dashboard.page';

describe('CommonDashboardPage', () => {
  let component: CommonDashboardPage;
  let fixture: ComponentFixture<CommonDashboardPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(CommonDashboardPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
