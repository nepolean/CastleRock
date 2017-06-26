import { Component, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Paginator } from './paginator';

@Component({
  moduleId: module.id,
  selector: 'abstract-paginator-component',
  templateUrl: './abstract-paginator.component.html'
})
export class AbstractPaginatorComponent {

  protected NUMBER_OF_PAGINATION_BUTTONS = 3;
  protected firstButtonNumber: number = 1;

  protected paginator: Paginator = new Paginator();
  protected pageChange: EventEmitter<Paginator> = new EventEmitter<Paginator>();

  protected goToNextPage(): void {
    if (this.paginator.last) {
        return;
    }
    this.paginator.currentPage ++;
    this.setButtonNumbers();
    this.pageChange.emit(this.paginator);
  }

  protected goToPreviousPage(): void {
    if (this.paginator.first) {
        return;
    }
    this.paginator.currentPage --;
    this.setButtonNumbers();
    this.pageChange.emit(this.paginator);
  }

  protected selectPage(selectedPage: number) {
      if (this.shouldBeDisabled(selectedPage)) {
        return;
      }
      this.paginator.currentPage = selectedPage - 1;
      this.setButtonNumbers();
      this.pageChange.emit(this.paginator);
  }

  protected setButtonNumbers(): void {
      if (+this.paginator.currentPage + 1 > this.NUMBER_OF_PAGINATION_BUTTONS) {
        this.firstButtonNumber = +this.paginator.currentPage + 1;
      } else {
          this.firstButtonNumber = 1;
      }
  }

  protected shouldBeDisabled(buttonNumber: number): boolean {
      if (buttonNumber === +this.paginator.currentPage + 1) {
        return true;
      }
      if (buttonNumber > +this.paginator.totalPages) {
          return true;
      }
      return false;
  }

  protected getButtonClass(buttonNumber: number): string {
      if (buttonNumber === +this.paginator.currentPage + 1) {
          return 'paginator-button active';
      }
      if (buttonNumber > +this.paginator.totalPages) {
          return 'paginator-button disabled';
      }
      return 'paginator-button';
  }

}