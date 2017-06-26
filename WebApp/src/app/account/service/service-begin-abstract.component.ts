import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-user-service-begin',
  templateUrl: './service-begin-abstract.component.html'
})
export class ServiceBeginAbstractComponent {

  @Output() pageChange = new EventEmitter<string>();

  constructor(protected router: Router){}
}