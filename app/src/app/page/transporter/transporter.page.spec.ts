import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TransporterPage } from './transporter.page';

describe('TransporterPage', () => {
  let component: TransporterPage;
  let fixture: ComponentFixture<TransporterPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(TransporterPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
