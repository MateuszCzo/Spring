import { Component, OnInit } from '@angular/core';
import { HttpService } from '../services/http.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { EpidemicParamsPage } from '../model/response/EpidemicParamsPage';
import { HttpErrorResponse } from '@angular/common/http';
import { EpidemicParamsRes } from '../model/response/EpidemicParamsRes';
import {MatPaginatorModule, PageEvent} from '@angular/material/paginator';

@Component({
  selector: 'app-epidemic-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MatPaginatorModule],
  templateUrl: './epidemic-list.component.html',
  styleUrl: './epidemic-list.component.css'
})
export class EpidemicListComponent implements OnInit {
  searchForm = this.formBuilder.group({
    search: [null, Validators.required]
  });
  isSubmited = false;

  epidemicList: Array<EpidemicParamsRes> = [];
  count: number = 0;

  length = 50;
  page = 0;
  pageSize = 10;

  pageEvent?: PageEvent;

  constructor(private httpService: HttpService, private formBuilder: FormBuilder ) {};

  ngOnInit() {
    this.getEpidemicPage();
  }

  private handlePageError(err: HttpErrorResponse, caught: Observable<EpidemicParamsPage>): Observable<never> {
    alert('An error occured');
    this.resetView();

    return throwError(() => `An error occured: ${err.error.message}`);
  }

  searchEpidemic() {
    if (this.searchForm.invalid) return;
    this.isSubmited = true;
    this.httpService.searchEpidemicParams(this.searchForm.value.search!, this.page, this.pageSize, (err, cought) => this.handleSearchError(err, cought))
      .subscribe(epidemicPage => {
        this.epidemicList = epidemicPage.params;
        this.count = epidemicPage.count;
        console.log("epidemicPage", epidemicPage);
      });
  }

  getEpidemicPage() {
    this.httpService.getEpidemicParamsPage(this.page, this.pageSize, (err, cought) => this.handlePageError(err, cought))
      .subscribe(epidemicPage => {
        this.epidemicList = epidemicPage.params;
        this.count = epidemicPage.count;
      });
  }

  private handleSearchError(err: HttpErrorResponse, caught: Observable<EpidemicParamsPage>): Observable<never> {
    alert('An error occured');
    this.resetView();

    return throwError(() => `An error occured: ${err.error.message}`);
  }

  private resetView() {
    this.count = 0;
    this.page = 0;
    this.epidemicList = [];
    this.isSubmited = false;
    this.searchForm.reset();
  }

  public isFieldInvalid(fieldName: string): boolean {
    let field = this.searchForm.get(fieldName);

    return Boolean(field?.invalid &&
      (field?.dirty || field?.touched || this.isSubmited));
  }

  public clearSearch() {
    this.resetView()
    this.getEpidemicPage();
  }

  public deleteEpidemic(id: number): void {
    this.httpService.deleteEpidemic(id, (err, caught) => this.handleDeleteError(err, caught))
      .subscribe(() => {this.searchForm.value.search ? this.searchEpidemic() : this.getEpidemicPage()});
  }

  private handleDeleteError(err: HttpErrorResponse, caught: Observable<never>): Observable<never> {
    alert('An error occured');
    this.resetView();

    return throwError(() => `An error occured: ${err.error.message}`);
  }

  public getNextPage(): void {
    if (this.count / this.pageSize >= this.page) return;
    this.searchForm.value.search ? this.searchEpidemic() : this.getEpidemicPage()
  }

  public getPrevPage(): void {
    if (this.page <= 0) return;
    this.page++;
    this.searchForm.value.search ? this.searchEpidemic() : this.getEpidemicPage()
  }

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.length = e.length;
    this.pageSize = e.pageSize;
    this.page = e.pageIndex;
    this.searchForm.value.search ? this.searchEpidemic() : this.getEpidemicPage()
  }
}
