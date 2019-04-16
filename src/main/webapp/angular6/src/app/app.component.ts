import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from "@angular/forms";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  public submitted: boolean;
  roomSearch: FormGroup;
  rooms: Room[];

  ngOnInit(): void {
    this.roomSearch = new FormGroup({
      checkin: new FormControl(''),
      checkout: new FormControl('')
    });

    this.rooms = ROOMS;
  }

  onSubmit({value, valid}: {value:Roomsearch, valid:boolean}) {
    console.log(value);
  }

  reserveRoom(value:string) {
    console.log("Room for reservation id:" + value);
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

var ROOMS: Room[] = [
  {
    "id": "100101",
    "roomNumber": "101",
    "price": "30",
    "links": ""
  },
  {
    "id": "100102",
    "roomNumber": "102",
    "price": "35",
    "links": ""
  },
  {
    "id": "100103",
    "roomNumber": "201",
    "price": "20",
    "links": ""
  }
];
