import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpService } from '../services/http.service';
import { Observable, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { EpidemicParamsRes } from '../model/response/EpidemicParamsRes';
import { EpidemicParamsReq } from '../model/request/EpidemicParamsReq';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-epidemic-create',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, CommonModule, MatFormFieldModule, MatSelectModule],
  templateUrl: './epidemic-create.component.html',
  styleUrl: './epidemic-create.component.css'
})
export class EpidemicCreateComponent implements OnInit {
  epidemicSimForm = this.formBuilder.group({
    name:             [null, Validators.required],
    population:       [null, Validators.required],
    infected:         [null, Validators.required],
    r:                [null, Validators.required],
    mortality:        [null, Validators.required],
    recoveryTime:     [null, Validators.required],
    deathTime:        [null, Validators.required],
    simulationTime:   [null, Validators.required],
    algorithm:        [null, Validators.required],
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
    simulationTime: ''
  }

  algorithms: Array<string> = [];

  public constructor(private http: HttpService, private router: Router, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.http.getEpidemicAlgorithms((err, caught) => this.handleAlgorithmsError(err, caught))
      .subscribe(algorithms => this.algorithms = algorithms);
  }

  private handleAlgorithmsError(err: HttpErrorResponse, caught: Observable<Array<string>>): Observable<never> {
    if (err.status == 400) {
      this.displayErrors(err);
    }
    return throwError(() => `An error occured: ${err.error.message}`);
  }

  public simulateEpidemic(): void {
    this.isSubmited = true;

    if (this.epidemicSimForm.invalid) return;

    let form = this.epidemicSimForm.value;

    let params: EpidemicParamsReq = {
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


    this.http.simulateEpidemic(params, (err, cought) => this.handleSimulationError(err, cought))
      .subscribe(epidemicParameters => {
        this.router.navigate([`/epidemic/view/${epidemicParameters.id}`]);
      });
  }

  private handleSimulationError(err: HttpErrorResponse, caught: Observable<EpidemicParamsRes>): Observable<never> {
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
