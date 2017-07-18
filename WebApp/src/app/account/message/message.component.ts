import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Paginator } from '../../common/page-utils/paginator/paginator';
import { AccountPaginatorComponent } from '../page-sections/paginator/account-paginator.component';

@Component({
  moduleId: module.id,
  selector: 'app-account-message',
  templateUrl: 'message.component.html'
})
export class MessageComponent {

    private paginator: Paginator = new Paginator();
    constructor(
        private router: Router,
        private titleService: Title) {
        this.paginator.totalPages = 1;
        this.paginator.totalElements = 3;
        this.paginator.last = true;
        this.paginator.first = true;
        this.paginator.size = 3;
        this.paginator.currentPage = 0;
        this.paginator.numberOfElements = 3;
        this.paginator.entity = 'messages';
        this.titleService.setTitle('Company | Messages');
    }

    private handlePageSelection(paginator: Paginator): void {

    }
}