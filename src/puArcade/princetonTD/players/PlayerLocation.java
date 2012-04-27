package puArcade.princetonTD.players;

import java.io.Serializable;

import android.graphics.Color;
import android.graphics.Rect;

public class PlayerLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	transient Player player;

	Rect zone;

	private int color;

	// Constructor
	public PlayerLocation(int id, Rect zone)
	{
		this(id,zone,Color.BLACK);
	}

	public PlayerLocation(int id,Rect zone, int color)
	{
		this.zone = zone;
		this.color = color;
		this.id = id;
	}

	public void setPlayer(Player player)
	{
		if(player == null)
		{
			if(this.player != null)
			{
				this.player.setPlayerLocation(null);
				this.player = null;
			}
		}

		else
		{
			if(this.player != null)
				throw new IllegalArgumentException("Occupied Location");
			else
			{ 
				if(player.getLocation() != null)
					player.getLocation().setPlayer(null);

				this.player = player;
				player.setPlayerLocation(this);
			}
		}   
	}

	public int getColor()
	{
		return color;
	}

	public Rect getZone()
	{
		return zone;
	}

	public Player getPlayer()
	{
		return player;
	}

	public int getId()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return "Zone "+id;
	}

	public void removePlayer()
	{
		if(this.player != null)
		{
			this.player.withdrawPlayerLocation();
			this.player = null;
		}
	}

	public void setColor(int color)
	{
		this.color = color;
	}

}
