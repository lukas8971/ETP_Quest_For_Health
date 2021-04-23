import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {ErrorData} from '../../entity/error-data';

@Component({
  selector: 'app-error-dialog',
  templateUrl: './error-dialog.component.html',
  styleUrls: ['./error-dialog.component.css']
})
export class ErrorDialogComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: ErrorData) {
    if (data.err === null && data.message !== undefined) {
      this.data.message = data.message;
    } else {
      if (data.err.status === 0) {
        // If status is 0, the backend is probably down
        this.data.message = 'The backend seems not to be reachable';
      } else if (data.err.error.message === 'No message available') {
        // If no detailed error message is provided, fall back to the simple error name
        this.data.message = data.err.error.error;
      } else {
        this.data.message = data.err.error.message;
      }
    }
  }

  ngOnInit(): void {
  }

}
