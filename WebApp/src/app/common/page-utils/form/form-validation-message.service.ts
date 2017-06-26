import { Injectable } from '@angular/core';

@Injectable()
export class FormValidationMessageService {

  getRequiredMessage(fieldName: string): string {
    return fieldName + ' is required.';
  }

  getMinLengthMessage(fieldName: string, minLength: number): string {
    return fieldName + ' must be at least ' + minLength + ' characters long.';
  }

  getMaxLengthMessage(fieldName: string, maxLength: number): string {
    return fieldName + ' cannot be more than ' + maxLength + ' characters long.';
  }

  getInvalidEmailMessage(): string {
    return 'Email address is invalid.';
  }

  getSpecialCharactersNotAllowedMessage(): string {
    return 'Special characters not allowed.';
  }

  getAllowedUsernameMessage(): string {
    return 'Username can contain numbers, letters of the alphabet and _ only.';
  }

  getPasswordMismatchMessage(): string {
    return "Passwords don't match";
  }

  getMinimumValueMessage(fieldName: string, minAllowedValue: number): string {
    return 'Minimum allowed value for ' + fieldName + ' is ' + minAllowedValue;
  }

  getMaximumValueMessage(fieldName: string, maxAllowedValue: number): string {
    return 'Maximum allowed value for ' + fieldName + ' is ' + maxAllowedValue;
  }
}
