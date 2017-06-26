import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
@Component({
  moduleId: module.id,
  selector: 'app-account-ticket',
  templateUrl: 'ticket.component.html'
})
export class TicketComponent {

    constructor(
        private router: Router,
        private titleService: Title) {
        this.titleService.setTitle('Company | Help Center');
    }
}