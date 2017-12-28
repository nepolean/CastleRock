import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Paginator } from '../../../common/page-utils/paginator/paginator';
import { AbstractPaginatorComponent } from '../../../common/page-utils/paginator/abstract-paginator.component';

@Component({
  moduleId: module.id,
  selector: 'admin-account-paginator',
  templateUrl: '../../../common/page-utils/paginator/abstract-paginator.component.html'
})
export class AdminAccountPaginatorComponent extends AbstractPaginatorComponent {

  @Input() paginator: Paginator = new Paginator();
  @Output() pageChange = new EventEmitter<Paginator>();

}