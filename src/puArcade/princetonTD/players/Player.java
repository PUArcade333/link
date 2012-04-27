package puArcade.princetonTD.players;

import puArcade.princetonTD.game.Score;

public class Player {
	
private static int currentID = 0;
    
    private int id;
    
    private String alias;
    
    private double gold = 0;
    
    private Team team;
   
    private Score score = new Score();
    
    private PlayerLocation location;

    private PlayerState ps;
 
    // offside
    private boolean offside;

    public Player(String alias)
    {
        this.alias = alias;
        this.id = ++currentID;
    }

    public int getId()
    {
        return id;
    }
    
    public int getScore()
    {
        return score.getValue();
    }
    
    public boolean isOffside()
    {
        return offside;
    }

    public void setOffside()
    {
        offside = true;
    }
    
    public void setGold(double gold)
    {
        this.gold = gold;
        
        if(ps != null)
            ps.updatePlayer(this);
    }

    public double getGold()
    {
        return gold;
    }

    public void setScore(int score)
    {
        this.score.setValue(score);
        
        if(ps != null)
            ps.updatePlayer(this);
    }
    
    public Team getTeam()
    {
        return team;
    }
    
    public PlayerLocation getLocation()
    {
        return location;
    }
     
    public boolean hasLost()
    {
        return team.getLives() <= 0;
    }

    public String getAlias()
    {
        return alias;
    }
    
    public void setTeam(Team team)
    {
        if(this.team != null && this.team.contains(this))
            this.team.removePlayer(this);
        
        this.team = team;
        
        // FIXME
        //team.addPlayer(this);
    }
    
    public void setPlayerLocation(PlayerLocation playerLocation)
    {
        if(playerLocation != null 
        && playerLocation.getPlayer() != this)
            playerLocation.setPlayer(this);
        
        this.location = playerLocation;
    }

    public void withdrawPlayerLocation()
    {
        if(location != null && location.getPlayer() != this) 
            location.removePlayer();
        
        location = null;
    }

    public void setId(int id)
    {
        this.id = id; 
    }

    public void setPlayerState(PlayerState ps)
    {
        this.ps = ps;
    }
	
}
