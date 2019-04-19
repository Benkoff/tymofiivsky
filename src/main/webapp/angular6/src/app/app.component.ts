import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from "@angular/forms";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  constructor(private http:HttpClient) {}

  private baseUrl:string = 'http://localhost:8080';
  roomSearch: FormGroup;
  rooms: Room[];
  currentCheckInVal:string;
  currentCheckOutVal:string;

  ngOnInit(): void {
    this.roomSearch = new FormGroup({
      checkin: new FormControl(''),
      checkout: new FormControl('')
    });

    this.roomSearch.valueChanges.subscribe(valChange => {
      this.currentCheckInVal = valChange.checkin;
      this.currentCheckOutVal = valChange.checkout;
    });

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    // Loop over them and prevent submission
    let forms = document.getElementsByClassName('needs-validation');
    Array.prototype.filter.call(forms, function(form) {
      form.addEventListener('submit', function(event) {
        if (form.checkValidity() === false) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add('was-validated');
      }, false);
    });
  }

  onSubmit({value, valid}: {value:Roomsearch, valid:boolean}) {
    if (!valid) {
      return;
    }
    this.getAll()
      .subscribe(
        rs => {
          this.rooms = rs;

          console.log('Rooms List:');
          this.rooms.forEach(item => console.log(item.id, item.roomNumber, item.price));
        },
        err => console.log(err)
      );
  }

  reserveRoom(value:string) {
    console.log("Room for reservation id:" + value);
    this.createReservation(new ReserveRoomRequest(value, this.currentCheckInVal, this.currentCheckOutVal));
  }

  createReservation(body: ReserveRoomRequest) {
    let bodyString = JSON.stringify(body);
    let headers = new HttpHeaders().set('Content-Type', 'application/json');

    this.http.post(this.baseUrl + '/v1/reservations', bodyString, {headers:headers})
      .subscribe(res => console.log(res), error => console.log("Error", error));
  }

  getAll():Observable<Room[]> {
    return this.http.get<Room[]>(this.baseUrl
      + '/v1/reservations/rooms?checkin=' + this.currentCheckInVal + '&checkout=' + this.currentCheckOutVal)
      .pipe(map(res => res['content']));
  }
}

export interface Roomsearch {
  checkin:string;
  checkout:string;
}

export interface Room {
  id:string;
  roomNumber:string;
  price:string;
  links:string;
}

export class ReserveRoomRequest {
  roomId: string;
  checkin: string;
  checkout: string;

  constructor(roomId: string, checkin: string, checkout: string) {
    this.roomId = roomId;
    this.checkin = checkin;
    this.checkout = checkout;
  }
}
