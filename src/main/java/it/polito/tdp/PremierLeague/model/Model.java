package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model 
{
	private PremierLeagueDAO dao;
	private Map<Integer, Player> giocatoriAll;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Match partita;
	
	public Model()
	{
		this.dao = new PremierLeagueDAO();
		this.giocatoriAll = new HashMap<Integer, Player>();
		this.giocatoriAll = this.dao.mapAllPlayers();
		this.partita = null;
	}
	
	/**
	 * 
	 * @return lista di tutte le partite
	 */
	public List<Match> listAllMatches()
	{
		List<Match> partite = new LinkedList<Match>();
		partite = this.dao.listAllMatches();
		Collections.sort(partite);
		
		return partite;
	}
	
	/**
	 * 
	 * @param m partita
	 * Crea il grafo relativo alla partita m passata
	 */
	public void creaGrafo(Match m)
	{
		this.partita = m;
		
		// creo il grafo vuoto
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungere i vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici(partita, giocatoriAll));
		
		//aggiungere gli archi
		for(Adiacenza a: dao.getAdiacenze(partita, giocatoriAll))
		{
			if(a.getPeso() >= 0)
			{
				// P1 migliore di P2
				// --> arco da P1 a P2
				
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2()))
				{
					Graphs.addEdgeWithVertices(grafo, a.getP1(), a.getP2(), a.getPeso());
				}
			}
			else
			{
				// P2 migliore di P1
				// --> arco da P2 a P1
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) 
				{
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), (-1) * a.getPeso());
				}
			}
		}
	}
	
	public int nVertici()
	{
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi()
	{
		return this.grafo.edgeSet().size();
	}
	
	public GiocatoreMigliore getMigliore()
	{
		if(this.grafo == null)
		{
			return null;
		}
		
		Player best = null;
		double maxDelta = Double.MIN_VALUE;
		
		for(Player p: this.grafo.vertexSet())
		{
			// per ogni giocatore vertice del grafo
			
			// somma dei pesi degli archi uscenti
			double sumOut = 0;
			for(DefaultWeightedEdge arco: this.grafo.outgoingEdgesOf(p))
			{
				sumOut = sumOut + this.grafo.getEdgeWeight(arco);
			}
			
			
			// somma dei pesi degli archi entranti
			double sumIn = 0;
			for(DefaultWeightedEdge arco: this.grafo.incomingEdgesOf(p))
			{
				sumIn = sumIn + this.grafo.getEdgeWeight(arco);
			}
			
			
			// calcolo il delta e guardo se è il massimo
			double delta = sumOut - sumIn;
			
			if(delta > maxDelta)
			{
				best = p;
				maxDelta = delta;
			}
		}
		
		return new GiocatoreMigliore(best, maxDelta);
	}

	public OutputSim simula(int azioni)
	{
		if(this.partita == null)
		{
			// il grafo non è ancora stato creato
			return null;
		}
		
		Player best = this.getMigliore().getP();
		Team squadraBest = dao.getTeamGiocatore(best, partita);
		
		Simulatore sim = new Simulatore();
		
		sim.init(azioni, partita, squadraBest);
		sim.run();
		
		return sim.getOutputSim();
	}

}
