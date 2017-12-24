import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-account-utility-bill-begin',
  templateUrl: './utility-bill-begin-abstract.component.html'
})
export class UtilityBillBeginAbstractComponent {

  @Output() pageChange = new EventEmitter<string>();

  constructor(protected router: Router){}
}