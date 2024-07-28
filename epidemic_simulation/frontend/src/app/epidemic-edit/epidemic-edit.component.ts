import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HttpService } from '../services/http.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { EpidemicParamsRes } from '../model/response/EpidemicParamsRes';
import { EpidemicParamsReqPut } from '../model/request/EpidemicparamsReqPut';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-epidemic-edit',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, CommonModule, MatFormFieldModule, MatSelectModule],
  templateUrl: './epidemic-edit.component.html',
  styleUrl: './epidemic-edit.component.css'
})
export class EpidemicEditComponent implements OnInit {
  epidemicSimForm = this.formBuilder.group({
    id:               [NaN, Validators.required],
    name:             ['', Validators.required],
    population:       [NaN, Validators.required],
    infected:         [NaN, Validators.required],
    r:                [NaN, Validators.required],
    mortality:        [NaN, Validators.required],
    recoveryTime:     [NaN, Validators.required],
    deathTime:        [NaN, Validators.required],
    simulationTime:   [NaN, Validators.required],
    algorithm:        ['', Validators.required],
  });

  isSubmited = false;

  messages: Record<string, string> = {
    main: '',
    name: '',
    population: '',
    infected: '',
    r: '',
    mortality: '',
    recoveryTime: '',
    deathTime: '',
    simulationTime: '',
    algorithm: ''
  }

  id: number;
  
  algorithms: Array<string> = [];

  public constructor(private http: HttpService, private router: Router, private formBuilder: FormBuilder, private activatedRoute: ActivatedRoute) {
    this.id = parseInt(this.activatedRoute.snapshot.paramMap.get('id')!);
  }

  public ngOnInit(): void {
    this.http.getEpidemicParams(this.id, (err, cought) => this.handleParamsError(err, cought))
      .subscribe(params => {
        this.initForm(params);
      });

    this.http.getEpidemicAlgorithms((err, caught) => this.handleAlgorithmsError(err, caught))
      .subscribe(algorithms => this.algorithms = algorithms);
  }

  private handleAlgorithmsError(err: HttpErrorResponse, caught: Observable<Array<string>>): Observable<never> {
    if (err.status == 400) {
      this.displayErrors(err);
    }
    return throwError(() => `An error occured: ${err.error.message}`);
  }

  private handleParamsError(err: HttpErrorResponse, caught: Observable<EpidemicParamsRes>): Observable<never> {
    alert(err.error.error ?? 'An error occured');
    
    this.router.navigate(["/epidemic"]);

    return throwError(() => `An error occured: ${err.error.message}`);
  }

  public initForm(params: EpidemicParamsRes): void {
    // todo algorithm
    let newData = {
      id:               params.id,
      name:             params.name,
      population:       params.population,
      infected:         params.infected,
      r:                params.r,
      mortality:        params.mortality,
      recoveryTime:     params.recoveryTime,
      deathTime:        params.deathTime,
      simulationTime:   params.simulationTime,
      algorithm:        null,
    }
    this.epidemicSimForm.setValue(newData);
  }

  public putEpidemic(): void {
    this.isSubmited = true;

    if (this.epidemicSimForm.invalid) return;

    let form = this.epidemicSimForm.value;

    let params: EpidemicParamsReqPut = {
      id:               form.id!,
      name:             form.name!,
      population:       form.population!,
      infected:         form.infected!,
      r:                form.r!,
      mortality:        form.mortality!,
      recoveryTime:     form.recoveryTime!,
      deathTime:        form.deathTime!,
      simulationTime:   form.simulationTime!,
      algorithmType:    form.algorithm!,
    };


    this.http.putEpidemic(params, (err, cought) => this.handlePutSimulationError(err, cought))
      .subscribe(epidemicParameters => {
        this.router.navigate([`/epidemic/view/${epidemicParameters.id}`]);
      });
  }

  private handlePutSimulationError(err: HttpErrorResponse, caught: Observable<EpidemicParamsRes>): Observable<never> {
    if (err.status == 400) {
      this.displayErrors(err);
    }
    return throwError(() => `An error occured: ${err.error.message}`);
  }

  private displayErrors(err: HttpErrorResponse): void {
    for (const key in err.error) {
      if (err.error.hasOwnProperty(key) && this.messages.hasOwnProperty(key)) {
        this.messages[key] = err.error[key];
      }
    }
    if (err.error.epidemicParamsReq) {
      this.messages['main'] = err.error.epidemicParamsReq;
    }
  }

  public isFieldInvalid(fieldName: string): boolean {
    let field = this.epidemicSimForm.get(fieldName);

    return Boolean(field?.invalid &&
      (field?.dirty || field?.touched || this.isSubmited));
  }

  public isFieldRequired(fieldName: string): boolean {
    let field = this.epidemicSimForm.get(fieldName);

    return Boolean(field?.hasError('required') &&
      (field?.dirty || field?.touched || this.isSubmited));
  }
}
