import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-account-property-begin',
  templateUrl: './property-begin-abstract.component.html'
})
export class PropertyBeginAbstractComponent {

  @Output() pageChange = new EventEmitter<string>();

  constructor(protected router: Router){}
}