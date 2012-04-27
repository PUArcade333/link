package puArcade.princetonTD.game;

import java.io.Serializable;
import java.util.Date;

public class Score implements Serializable, Comparable<Score> {

	private static final long serialVersionUID = 1L;

	private int value;
	private String playerName;
	private Date date;
	// duration
	private long duration;

	// Constructor
	public Score (String playerName, int value, long duration)
	{
		this.value = value;
		this.playerName = playerName;
		this.duration = duration;

		date = new Date();
	}

	public Score (Score score)
	{
		value		= score.value;
		playerName	= score.playerName;
		duration	= score.duration;
		date		= score.date;
	}

	public Score()
	{
		playerName = "";
	}

	public int getValue ()
	{
		return value;
	}

	public String getPlayerName ()
	{
		return playerName;
	}

	public long getDuration()
	{
		return duration;
	}

	public Date getDate()
	{
		return new Date(date.getTime());
	}

	public int compareTo (Score comp)
	{
		if (comp.value > value)
			return 1;
		else if (comp.value == value)
			return 0;
		else
			return -1;
	}

	@Override
	public String toString ()
	{
		return playerName + " - " + value + " - " + date;
	}

	public void setValue(int score)
	{
		value = score;   
	}

	private int getSeconds()
	{
		return (int) (duration) % 60;
	}

	private int getMinutes()
	{
		return (int) (duration / 60) % 60;
	}

	private int getHours()
	{
		return (int) (duration / 3600) % 24;
	}

	public String getHMS()
	{
		return String.format("%02d:%02d:%02d", getHours(), 
				getMinutes(), 
				getSeconds());
	}

}
