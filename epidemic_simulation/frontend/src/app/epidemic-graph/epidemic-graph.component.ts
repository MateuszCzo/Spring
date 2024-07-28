import { AfterViewInit, Component, ElementRef, Input, OnInit } from '@angular/core';
import { Color, colorSets, NgxChartsModule } from '@swimlane/ngx-charts';
import { EpidemicSim } from '../model/response/EpidemicSim';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-epidemic-graph',
  standalone: true,
  imports: [NgxChartsModule, CommonModule],
  templateUrl: './epidemic-graph.component.html',
  styleUrl: './epidemic-graph.component.css'
})
export class EpidemicGraphComponent implements OnInit {

  @Input()
  epidemicSimObs!: Observable<Array<EpidemicSim>>;

  data: Array<{name: string, series: Array<{name: string, value: number}>}> = [];

  view: [number, number] = [1000, 400];

  legend: boolean = true;
  showLabels: boolean = true;
  animations: boolean = false;
  xAxis: boolean = true;
  yAxis: boolean = true;
  showYAxisLabel: boolean = true;
  showXAxisLabel: boolean = true;
  xAxisLabel: string = 'Day';
  yAxisLabel: string = 'People';
  timeline: boolean = true;

  colorScheme: Color = colorSets[0];

  ngOnInit(): void {
    this.epidemicSimObs.subscribe((data) => this.bindData(data));
  }

  public bindData(epidemicSim: Array<EpidemicSim>): void {
    let healthy:  {name: string, series: Array<{name: string, value: number}>} = {name: "Healthy",  series: []};
    let infected: {name: string, series: Array<{name: string, value: number}>} = {name: "Infected", series: []};
    let dead:     {name: string, series: Array<{name: string, value: number}>} = {name: "Dead",     series: []};
    let immune:   {name: string, series: Array<{name: string, value: number}>} = {name: "Immune",   series: []};

    epidemicSim?.forEach(sim => {
      healthy.series.push({name: sim.day.toString(), value: sim.healthy});
      infected.series.push({name: sim.day.toString(), value: sim.infected});
      dead.series.push({name: sim.day.toString(), value: sim.dead});
      immune.series.push({name: sim.day.toString(), value: sim.immune});
    });

    this.data = Array.of(healthy, infected, dead, immune);
  }

  public onSelect(event: Event): void { }
  public onActivate(event: Event): void { }
  public onDeactivate(event: Event): void { }
}
