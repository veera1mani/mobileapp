import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserReturnPage } from './user-return.page';

describe('UserReturnPage', () => {
  let component: UserReturnPage;
  let fixture: ComponentFixture<UserReturnPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(UserReturnPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
