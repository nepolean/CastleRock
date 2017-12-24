import { Component, Input, Output, EventEmitter,OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router,ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
@Component({
  moduleId: module.id,
  selector: 'app-user-service-begin',
  templateUrl: './service-begin-abstract.component.html'
})
export class ServiceBeginAbstractComponent implements OnInit {
  private sub: Subscription;
  private proertyName:any;
  @Output() pageChange = new EventEmitter<string>();

  constructor(protected router: Router,private _route: ActivatedRoute){}
  ngOnInit(): void {
    // this.sub = this._route.params.subscribe(
    //     params => {
    //         let name = params['name'];
    //         this.proertyName=name;
    //         if(this.proertyName==undefined)
    //         {this.proertyName="Purva Supreme"}
    // });
    this.proertyName=localStorage.getItem('appType');
}
}