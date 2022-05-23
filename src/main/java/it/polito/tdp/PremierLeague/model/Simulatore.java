package it.polito.tdp.PremierLeague.model;

import java.util.PriorityQueue;


import it.polito.tdp.PremierLeague.model.Evento.EventType;

public class Simulatore 
{
	// CODA DEGLI EVENTI
	private PriorityQueue<Evento> coda;
		
	// PARAMETRI DI SIMULAZIONE
	private int azioni;
	private int countEventi;
	
	
	// OUTPUT DELLA SIMULAZIONE
	private OutputSim stat;
	
	// STATO DEL MONDO SIMULATO
	private Match partita;
	private Team squadraGiocatoreBest;
	private boolean bestIsAtHome;
	
	// METODI
	
	// COSTRUTTORE
	public Simulatore() 
	{
		
	}
	
	// INIZIALIZZARE LA SIMULAZIONE
	public void init(int azioni, Match partita, Team squadraBest)
	{
		this.azioni = azioni;
		this.partita = partita;
		this.squadraGiocatoreBest = squadraBest;
		this.stat = new OutputSim(partita);
		this.countEventi = 0;
		
		this.coda = new PriorityQueue<>();
		
		if(partita.teamHomeID == squadraBest.teamID)
		{
			bestIsAtHome = true;
		}
		else
		{
			bestIsAtHome = false;
		}
		
		creaEventi(azioni);
	}
	
	private void creaEventi(int num) 
	{
		for(int i=0; i<num; i++)
		{
			double rand = Math.random();
			
			if(rand < 0.5)
			{
				// 50% --> gol
				Evento e = new Evento(EventType.GOAL, this.countEventi);
				System.out.println("Aggiunto gol");
				this.coda.add(e);
			}
			else if(rand < 0.8)
			{
				// 30% --> espulsione
				Evento e = new Evento(EventType.ESPULSIONE, this.countEventi);
				System.out.println("Aggiunte espulsione");
				this.coda.add(e);
			}
			else
			{
				// 20% --> infortunio
				Evento e = new Evento(EventType.INFORTUNIO, this.countEventi);
				System.out.println("Aggiunto infortunio");
				this.coda.add(e);
			}
			
			this.countEventi++;
		}
	}

	// ESECUZIONE DELLA SIMULAZIONE
	public void run()
	{
		// fin quando la coda non è vuota
		while(!coda.isEmpty())
		{
			// estraggo l'evento e lo elaboro
			Evento e = coda.poll();
			
			processaEvento(e);
		}
	}

	// ELABORAZIONE DEGLI EVENTI
	private void processaEvento(Evento e) 
	{
		switch (e.getTipo())
		{
		case ESPULSIONE:
			double rand = Math.random();
			if(rand < 0.6)
			{
				// 60% --> espulso uno della squadra del giocatore migliore
				if(bestIsAtHome)
				stat.plusEspCasa();
				else
				stat.plusEspOspiti();
			}
			else
			{
				// 40% --> espulso uno dell'altra squadra
				if(bestIsAtHome)
				stat.plusEspOspiti();
				else
				stat.plusEspCasa();
			}
			
			break;
			
		case GOAL:
			if(stat.getEspOspiti() == stat.getEspCasa())
			{
				// le squadre hanno lo stesso numero di giocatori in campo
				// -> il gol è sempre della squadra del giocatore migliore
				if(bestIsAtHome)
				stat.plusGolCasa();
				else
				stat.plusGolOspiti();
			}
			else
			{
				// segna sempre la squadra che ha più giocatori in campo
				if(stat.getEspCasa() > stat.getEspOspiti())
					stat.plusGolOspiti();
				else
					stat.plusGolCasa();
			}
			
			break;
			
		case INFORTUNIO:
			// devo creare 2 o 3 azioni nuove
			int numNuoveAzioni = 0;
			double randi = Math.random();
			
			if(randi < 0.5)
				numNuoveAzioni = 2;
			else
				numNuoveAzioni = 3;
			
			this.creaEventi(numNuoveAzioni);
			
			stat.plusTotInfortuni();
			
			break;
			
		default:
			break;
		}
	}
	
	// GETTER PER OTTENERE IL RISULTATO DELLA SIMULAZIONE
	public OutputSim getOutputSim()
	{
		return this.stat;
	}
}
