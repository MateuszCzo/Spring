import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { catchError, Observable } from 'rxjs';
import { EpidemicSim } from '../model/response/EpidemicSim';
import { EpidemicParamsPage } from '../model/response/EpidemicParamsPage';
import { EpidemicParamsRes } from '../model/response/EpidemicParamsRes';
import { EpidemicParamsReq } from '../model/request/EpidemicParamsReq';
import { EpidemicParamsReqPut } from '../model/request/EpidemicparamsReqPut';

@Injectable({
  providedIn: 'root'
})

export class HttpService {

  url = environment.backendUrl;

  constructor(private http: HttpClient) { }

  getEpidemicParamsPage(page: number, pageSize: number, 
    errorHandler: (err: any, caught: Observable<EpidemicParamsPage>) => Observable<never>
    ): Observable<EpidemicParamsPage> {
      
    let params = {
      'page': page,
      'pageSize': pageSize
    }

    return this.http
      .get<EpidemicParamsPage>(this.url + '/epidemic', {params})
      .pipe(catchError(errorHandler));
  }

  getEpidemicParams(id: number, 
    errorHandler: (err: any, caught: Observable<EpidemicParamsRes>) => Observable<never>
    ): Observable<EpidemicParamsRes> {

    return this.http
      .get<EpidemicParamsRes>(this.url + `/epidemic/${id}`)
      .pipe(catchError(errorHandler));
  }

  searchEpidemicParams(name: string, page: number, pageSize: number, 
    errorHandler: (err: any, caught: Observable<EpidemicParamsPage>) => Observable<never>
    ): Observable<EpidemicParamsPage> {

      let params = {
        'name': name,
        'page': page,
        'pageSize': pageSize
      }

    return this.http
      .get<EpidemicParamsPage>(this.url + '/epidemic/search', {params})
      .pipe(catchError(errorHandler));
  }

  getEpidemicSimulation(id: number,
    errorHandler: (err: any, caught: Observable<Array<EpidemicSim>>) => Observable<never>
    ): Observable<Array<EpidemicSim>> {

    return this.http
      .get<Array<EpidemicSim>>(this.url + `/epidemic/simulation/${id}`)
      .pipe(catchError(errorHandler));
  }

  simulateEpidemic(params: EpidemicParamsReq, 
    errorHandler: (err: any, caught: Observable<EpidemicParamsRes>) => Observable<never>
    ): Observable<EpidemicParamsRes> {

    return this.http
      .post<EpidemicParamsRes>(this.url + '/epidemic/simulation', params)
      .pipe(catchError(errorHandler));
  }

  deleteEpidemic(id: number,
    errorHandler: (err: any, cought: Observable<never>) => Observable<never>
    ): Observable<never> {
    
    return this.http.delete<never>(this.url + `/epidemic/${id}`)
      .pipe(catchError(errorHandler));
  }

  putEpidemic(params: EpidemicParamsReqPut,
    errorHandler: (err: any, caught: Observable<EpidemicParamsRes>) => Observable<never>
    ): Observable<EpidemicParamsRes> {

    return this.http.put<EpidemicParamsRes>(this.url + `/epidemic/${params.id}`, params)
      .pipe(catchError(errorHandler));
  }

  getEpidemicAlgorithms(errorHandler: (err: any, caught: Observable<Array<string>>) => Observable<never>
    ): Observable<Array<string>> {
      
    return this.http.get<Array<string>>(this.url + `/epidemic/algorithm`)
      .pipe(catchError(errorHandler));
  }
}
