import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AbstractPaginatorComponent } from 'app/common/page-utils/paginator/abstract-paginator.component';
import { Paginator } from 'app/common/page-utils/paginator/paginator';

@Component({
  moduleId: module.id,
  selector: 'admin-account-paginator',
  templateUrl: '../../../common/page-utils/paginator/abstract-paginator.component.html'
})
export class AdminAccountPaginatorComponent extends AbstractPaginatorComponent {

  @Input() paginator: Paginator = new Paginator();
  @Output() pageChange = new EventEmitter<Paginator>();

}