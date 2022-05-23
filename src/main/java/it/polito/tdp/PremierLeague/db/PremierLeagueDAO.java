package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	/**
	 * 
	 * @return mappa di tutti i giogatori
	 */
	public Map<Integer, Player> mapAllPlayers()
	{
		String sql = "SELECT * FROM Players";
		Map<Integer, Player> result = new HashMap<Integer, Player>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) 
			{

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.put(player.getPlayerID(), player);
			}
			conn.close();
			return result;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @return lista di tutte le squadre
	 */
	public List<Team> listAllTeams()
	{
		String sql = "SELECT * FROM Teams";
		List<Team> result = new ArrayList<Team>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) 
			{

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				result.add(team);
			}
			conn.close();
			return result;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @return lista di tutte le azioni
	 */
	public List<Action> listAllActions()
	{
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) 
			{
				
				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @return lista di tutte le partite
	 */
	public List<Match> listAllMatches()
	{
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) 
			{

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param m Match
	 * @param giocatoriAll mappa di tutti i giocatori
	 * @return lista dei giocatori della partita m
	 */
	public List<Player> getVertici(Match m, Map<Integer, Player> giocatoriAll)
	{
		List<Player> vertici = new LinkedList<Player>();
		
		String sql = "SELECT PlayerID "
				+ "FROM Actions "
				+ "WHERE MatchID = ?";
		
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, m.getMatchID());
			ResultSet res = st.executeQuery();
			
			while (res.next()) 
			{
				int id = res.getInt("PlayerID");
				Player player = giocatoriAll.get(id);

				vertici.add(player);
			}
			
			conn.close();
			return vertici;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param m partita
	 * @param giocatoriAll mappa di tutti i giocatori
	 * @return lista di coppie di giocatori e relativo delta efficienza
	 */
	public List<Adiacenza> getAdiacenze(Match m, Map<Integer,Player> giocatoriAll)
	{
		String sql = "SELECT a1.PlayerID as p1, a2.PlayerID as p2, "
				+ "((a1.totalSuccessfulPassesAll + a1.assists)/a1.timePlayed - (a2.totalSuccessfulPassesAll + a2.assists)/a2.timePlayed) as peso "
				+ "FROM Actions a1, Actions a2 "
				+ "WHERE a1.MatchID = a2.MatchID "
				+ "AND a1.MatchID = ? "
				+ "AND a1.PlayerID > a2.PlayerID "
				+ "AND a1.TeamID <> a2.TeamID";
		
		List<Adiacenza> ret = new LinkedList<Adiacenza>();
		
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, m.getMatchID());
			ResultSet res = st.executeQuery();
			
			while (res.next()) 
			{
				int id1 = res.getInt("p1");
				int id2 = res.getInt("p2");
				
				if(giocatoriAll.containsKey(id1) && giocatoriAll.containsKey(id2))
				{
					Player player1 = giocatoriAll.get(id1);
					Player player2 = giocatoriAll.get(id2);
					
					double peso = res.getDouble("peso");
					
					Adiacenza a = new Adiacenza(player1, player2, peso);

					ret.add(a);
				}
				
			}
			
			conn.close();
			return ret;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 
	 * @param p giocatore
	 * @param m partita
	 * @return squadra del giocatore in quella partita
	 */
	public Team getTeamGiocatore(Player p, Match m)
	{
		List<Team> squadre = this.listAllTeams();
		
		String sql = "SELECT TeamID "
				+ "FROM Actions "
				+ "WHERE PlayerID = ? "
				+ "AND MatchID = ?";
		
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, p.getPlayerID());
			st.setInt(2, m.getMatchID());
			ResultSet res = st.executeQuery();
			
			int id = -1;
			
			while (res.next()) 
			{
				id = res.getInt("TeamID");
			}
			
			conn.close();
			
			for(Team t: squadre)
			{
				if(t.getTeamID() == id)
				{
					return t;
				}
			}
			
			return null;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		
	}

}
