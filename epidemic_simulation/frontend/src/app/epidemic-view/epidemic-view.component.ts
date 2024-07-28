import { Component } from '@angular/core';
import { EpidemicParamsRes } from '../model/response/EpidemicParamsRes';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { EpidemicSim } from '../model/response/EpidemicSim';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpService } from '../services/http.service';
import { HttpErrorResponse } from '@angular/common/http';
import { EpidemicGraphComponent } from '../epidemic-graph/epidemic-graph.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-epidemic-view',
  standalone: true,
  imports: [CommonModule, EpidemicGraphComponent],
  templateUrl: './epidemic-view.component.html',
  styleUrl: './epidemic-view.component.css'
})
export class EpidemicViewComponent {

  id: number;

  epidemicParams?: EpidemicParamsRes;
  epidemicSimObs: BehaviorSubject<Array<EpidemicSim>>;

  constructor(private http: HttpService, private router: Router, private activatedRoute: ActivatedRoute) {
    this.id = parseInt(this.activatedRoute.snapshot.paramMap.get('id')!);
    this.epidemicSimObs = new BehaviorSubject<Array<EpidemicSim>>([]);
  }

  ngOnInit(): void {
    this.http.getEpidemicParams(this.id, (err, caught) => this.handleParamsError(err, caught))
      .subscribe(params => {
        this.epidemicParams = params;

        this.http.getEpidemicSimulation(this.id, (err, caught) => this.handleSimulationError(err, caught))
          .subscribe(sim => this.epidemicSimObs.next(sim));
      });
  }

  private handleParamsError(err: HttpErrorResponse, caught: Observable<EpidemicParamsRes>): Observable<never> {
    alert(err.error.error ?? 'An error occured');
    
    this.router.navigate(["/epidemic"]);

    return throwError(() => `An error occured: ${err.error.message}`);
  }

  private handleSimulationError(err: HttpErrorResponse, caught: Observable<Array<EpidemicSim>>): Observable<never> {
    alert(err.error.error ?? 'An error occured');
    
    this.router.navigate(["/epidemic"]);

    return throwError(() => `An error occured: ${err.error.message}`);
  }
}
