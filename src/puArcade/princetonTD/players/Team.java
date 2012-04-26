package puArcade.princetonTD.players;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import puArcade.princetonTD.exceptions.GameFullException;
import puArcade.princetonTD.exceptions.OccupiedLocationException;



import android.graphics.Rect;

public class Team implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String name;

	private int color;

	private Vector<Player> players = new Vector<Player>();

	private int lives;

	private ArrayList<Rect> startZone = new ArrayList<Rect>();

	private Rect endZone;

	private ArrayList<PlayerLocation> playerLocations = new ArrayList<PlayerLocation>();

	private double pathLength = 0.0;
	
	// Constructor
	public Team(int id, String name, int color)
	{
		this.id = id;
		this.name = name;
		this.color = color;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public int getColor()
	{
		return color;
	}

	public void addPlayer(Player player, PlayerLocation pl) 
			throws OccupiedLocationException
			{
		if(player == null)
			throw new IllegalArgumentException();

		if(pl == null)
			throw new IllegalArgumentException();

		if(pl.getPlayer() != null)
			throw new OccupiedLocationException("Location Occupied");

		if(player.getTeam() != null)
			player.getTeam().removePlayer(player);

		players.add(player);

		player.setTeam(this);

		player.setPlayerLocation(pl);
			}

	public void addPlayer(Player player) throws GameFullException
	{
		if(player == null)
			throw new IllegalArgumentException();

		PlayerLocation pl = findLocation();

		if(pl == null) 
			throw new GameFullException("No location available");

		else
			try{
				addPlayer(player, pl);
			} 
		catch (OccupiedLocationException e){
			e.printStackTrace();
		} 
	}

	public PlayerLocation findLocation()
	{
		for(PlayerLocation pl : playerLocations)
			if(pl.getPlayer() == null)
				return pl;

		return null;
	}

	public void removePlayer(Player player)
	{
		players.remove(player);

		if(player.getLocation() != null)
			player.getLocation().removePlayer();

		player.setTeam(null);
	}

	public boolean contains(Player player)
	{
		return players.contains(player);
	}


	public Vector<Player> getPlayers()
	{
		return players;
	}

	public int getScore()
	{
		int sum = 0;

		for(Player player : players)
			sum += player.getScore();

		return sum;
	}

	public int getLives()
	{
		return lives;
	}

	synchronized public void loseLife()
	{
		lives--;
	}

	public void setLives(int lives)
	{
		this.lives = lives;
	}

	public void addStartZone(Rect zone)
	{
		startZone.add(zone);
	}

	public Rect getStartZone(int index)
	{
		return startZone.get(index);
	}
	
	public Rect[] getStartZones()
	{
		Rect[] sz = new Rect[startZone.size()];
		startZone.toArray(sz);
		return sz;
	}

	public void setEndZone(Rect zone)
	{
		endZone = zone;
	}

	public Rect getEndZone()
	{
		return endZone;
	}

	public void addPlayerLocation(PlayerLocation pl)
	{
		playerLocations.add(pl);
	}

	public int getNLocations()
	{
		return playerLocations.size();
	}

	public ArrayList<PlayerLocation> getPlayerLocations()
	{
		return playerLocations;
	}

	@Override
	public String toString()
	{
		return name;
	}

	public double getPathLength()
	{
		return pathLength;
	}

	public void setPathLength(double length)
	{
		this.pathLength = length;
	}

	public boolean hasLost()
	{
		return lives <= 0 || isOffside();
	}

	public boolean isOffside()
	{
		Player player;
		Enumeration<Player> e = players.elements(); 
		while(e.hasMoreElements())
		{
			player = e.nextElement();

			if(!player.isOffside())
				return false; 
		}

		return true;
	}

	public void clear()
	{
		Player player;
		Enumeration<Player> e = players.elements(); 
		while(e.hasMoreElements())
		{
			player = e.nextElement();
			removePlayer(player);
		}

		players.clear();
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	public int getNStartZones()
	{
		return startZone.size();
	}

	public void removeLocation(PlayerLocation pl)
	{
		playerLocations.remove(pl);
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void removeStartZone(Rect z)
	{
		// TODO Protection ?
		// if(startZone.size() > 1)
		startZone.remove(z);
		// else
		//  throw new ... 
	}

}
