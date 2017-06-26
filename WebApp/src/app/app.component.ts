import { Component } from '@angular/core';

@Component({
  selector: 'res-app',
  template: `
  <header-section></header-section>
  <section id="dashboard" style="padding: 0px">
    <dashboard-section></dashboard-section>
  </section>
  <!-- #/Dashboard content -->
  <footer-section></footer-section>
  `
})
export class AppComponent {}