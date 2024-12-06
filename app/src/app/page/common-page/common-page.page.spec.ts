import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommonPagePage } from './common-page.page';

describe('CommonPagePage', () => {
  let component: CommonPagePage;
  let fixture: ComponentFixture<CommonPagePage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(CommonPagePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
