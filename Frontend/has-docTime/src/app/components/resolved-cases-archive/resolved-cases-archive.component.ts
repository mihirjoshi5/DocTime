import { Component, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SharedService } from 'src/app/services/shared.service';
import { PastAppointmentService } from 'src/app/services/past-appointment.service';
import { PastAppointment } from 'src/app/models/pastAppointment.model';
import { Token } from 'src/app/models/token.model';
import { AuthService } from 'src/app/services/auth.service';

interface SortByOption {
  label: string;
  value: string;
}

@Component({
  selector: 'app-resolved-cases-archive',
  templateUrl: './resolved-cases-archive.component.html',
  styleUrls: ['./resolved-cases-archive.component.css']
})
export class ResolvedCasesArchiveComponent implements OnInit{

  constructor(private pastAppointment: PastAppointmentService, private router: Router, private sharedService: SharedService, private route: ActivatedRoute, private authService : AuthService){}

  id : string = "";
  resolvedCases?: PastAppointment[];
  noDataFound : boolean = false;
  noDataFoundImg : string = "https://firebasestorage.googleapis.com/v0/b/ng-hasdoctime-images.appspot.com/o/dataNotFound.png?alt=media&token=2533f507-7433-4a70-989d-ba861273e537";
  page = 1;
  totalPages = 1;
  size = 5;
  sortBy = 'user.name';
  search = '';
  urlPath!: string;

  sizeOptions = [5, 10, 15];

  range(totalPages: number): number[] {
    return Array(totalPages).fill(0).map((_, index) => index + 1);
  }
  sortByOptions: SortByOption[] = [
    { label: 'StartTime', value: 'timeSlotForAppointment.startTime' },
    { label: 'User Name', value: 'user.name' }
  ];

  ngOnInit(): void {
    const decoded_token : Token = this.authService.decodeToken();
    this.id = decoded_token.id;

      this.getData();
  }

  getData(){
    let params: any = {};

    // Add query parameters based on selected options
    if (this.size) {
      params.size = this.size;
    }
    if (this.sortBy) {
      params.sortBy = this.sortBy;
    }
    if (this.search) {
      params.search = this.search;
    }
    params.page = this.page-1;

    this.pastAppointment.getPastAppointmentDataByDoctor(this.id, params).subscribe((data) =>{
      if(data.content.length !== 0) {
        this.resolvedCases = data.content;
        this.totalPages = data.totalPages;
        this.noDataFound = false;
      }else {
        this.noDataFound = true;
      }
    });
  }

  caseDetail(id: number){

    this.sharedService.setresolvedCaseDetailedData(this.resolvedCases?.[id]);
    this.router.navigate(['caseDetail'], {relativeTo : this.route});
  }

  onPageSizeChange() {
    this.page = 1;
    this.getData();
  }

  onSortByChange() {
    this.page = 1;
    this.getData();
  }

  onSearch() {
    this.page = 1;
    this.getData();
  }

  onPageChange(pageNumber: number) {
    this.page = pageNumber ;
    this.getData();
  }

}
