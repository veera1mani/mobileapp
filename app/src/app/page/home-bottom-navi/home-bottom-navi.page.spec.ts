import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HomeBottomNaviPage } from './home-bottom-navi.page';

describe('HomeBottomNaviPage', () => {
  let component: HomeBottomNaviPage;
  let fixture: ComponentFixture<HomeBottomNaviPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(HomeBottomNaviPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
