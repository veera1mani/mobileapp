import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReturnDetailsPage } from './return-details.page';

describe('ReturnDetailsPage', () => {
  let component: ReturnDetailsPage;
  let fixture: ComponentFixture<ReturnDetailsPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ReturnDetailsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
