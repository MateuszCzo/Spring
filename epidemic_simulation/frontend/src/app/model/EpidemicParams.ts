export interface EpidemicParams {
    name: string
    population: number;
    infected: number;
    r: number;
    mortality: number;
    recoveryTime: number;
    deathTime: number;
    simulationTime: number;
}