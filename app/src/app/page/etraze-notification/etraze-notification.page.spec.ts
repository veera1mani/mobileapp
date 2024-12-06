import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EtrazeNotificationPage } from './etraze-notification.page';

describe('EtrazeNotificationPage', () => {
  let component: EtrazeNotificationPage;
  let fixture: ComponentFixture<EtrazeNotificationPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(EtrazeNotificationPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
