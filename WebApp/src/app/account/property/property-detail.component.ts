import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router, Params } from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-property-detail',
  templateUrl: 'property-detail.component.html'
})
export class PropertyDetailComponent implements OnInit {

    private propertyId: number = 1;
    constructor(
        private router: Router,
        private titleService: Title,
        private route: ActivatedRoute) {
        this.titleService.setTitle('Company | Properties');
    }

    ngOnInit(): void {
        this.propertyId = this.route.params['id'];
        this.route.params.subscribe(params => {
            this.propertyId = +params['id'];
        });
    }
}