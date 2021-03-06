import { Injectable } from '@angular/core';
import {HttpClient, HttpParams,HttpErrorResponse} from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import {TokenstoreserviceService} from './tokenstoreservice.service';

@Injectable({
  providedIn: 'root'
})
export class LoginServiceService {

   url:string="http://localhost:8080/authenticate";
 // url:string="http://restapi-env.eba-xd2trzjb.us-east-2.elasticbeanstalk.com/authenticate";

  constructor(
    private http:HttpClient,
  ) { }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  };

  post(name:string,password:string){
    const params=new HttpParams()
                  .set('name',name)
                  .set('password',password);
      return this.http.post(this.url, params,{observe: "response"})
              .pipe(
                
                catchError(this.handleError)
              );
  }
}
