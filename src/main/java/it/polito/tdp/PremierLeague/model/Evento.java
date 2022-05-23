package it.polito.tdp.PremierLeague.model;

public class Evento implements Comparable<Evento>
{
	public enum EventType
	{
		GOAL,
		ESPULSIONE,
		INFORTUNIO
	}
	
	private EventType tipo;
	private int num;
	
	public Evento(EventType tipo, int num) 
	{
		super();
		this.tipo = tipo;
		this.num = num;
	}


	public EventType getTipo() {
		return tipo;
	}

	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}


	@Override
	public int compareTo(Evento other) 
	{
		return this.num - other.getNum();
	}

}
