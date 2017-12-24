import { Component,OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router,ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
@Component({
  moduleId: module.id,
  selector: 'app-account-service',
  templateUrl: 'service.component.html'
})
export class ServiceComponent implements OnInit{
    private sub: Subscription;
    private proertyName:any;
    constructor(
        private router: Router,
        private _route: ActivatedRoute,
        private titleService: Title) {
        this.titleService.setTitle('Company | Services');
    }
    ngOnInit(): void {
        this.sub = this._route.params.subscribe(
            params => {
                this.proertyName=localStorage.getItem('appType');
        });
        
    }
}