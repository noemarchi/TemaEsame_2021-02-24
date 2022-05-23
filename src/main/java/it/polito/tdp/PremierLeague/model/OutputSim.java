package it.polito.tdp.PremierLeague.model;

public class OutputSim 
{
	private Match partita;
	private int golCasa;
	private int golOspiti;
	private int espCasa;
	private int espOspiti;
	private int totInfortuni;
	
	public OutputSim(Match partita) 
	{
		super();
		this.partita = partita;
		this.golCasa = 0;
		this.golOspiti = 0;
		this.espCasa = 0;
		this.espOspiti = 0;
	}

	public Match getPartita() {
		return partita;
	}

	public void setPartita(Match partita) {
		this.partita = partita;
	}

	public int getGolCasa() {
		return golCasa;
	}

	public void plusGolCasa() {
		this.golCasa++;
	}

	public int getGolOspiti() {
		return golOspiti;
	}

	public void plusGolOspiti() {
		this.golOspiti++;
	}

	public int getEspCasa() {
		return espCasa;
	}

	public void plusEspCasa() {
		this.espCasa++;
	}

	public int getEspOspiti() {
		return espOspiti;
	}

	public void plusEspOspiti() {
		this.espOspiti++;
	}
	
	public int getTotInfortuni()
	{
		return this.totInfortuni;
	}
	
	public void plusTotInfortuni() {
		this.totInfortuni++;
	}

}
