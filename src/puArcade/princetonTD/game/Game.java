package puArcade.princetonTD.game;

import java.util.ArrayList;
import java.util.Vector;

import puArcade.princetonTD.animations.Animation;
import puArcade.princetonTD.animations.AnimationManager;
import puArcade.princetonTD.creatures.Creature;
import puArcade.princetonTD.creatures.CreatureManager;
import puArcade.princetonTD.creatures.CreatureState;
import puArcade.princetonTD.creatures.CreatureWave;
import puArcade.princetonTD.creatures.WaveState;
import puArcade.princetonTD.exceptions.BlockedPathException;
import puArcade.princetonTD.exceptions.GameFullException;
import puArcade.princetonTD.exceptions.GameInProgressException;
import puArcade.princetonTD.exceptions.InaccessibleZoneException;
import puArcade.princetonTD.exceptions.MaxLevelException;
import puArcade.princetonTD.exceptions.NoMoneyException;
import puArcade.princetonTD.exceptions.UnauthorizedException;
import puArcade.princetonTD.maps.Map;
import puArcade.princetonTD.players.Player;
import puArcade.princetonTD.players.PlayerLocation;
import puArcade.princetonTD.players.PlayerState;
import puArcade.princetonTD.players.Team;
import puArcade.princetonTD.towers.Tower;
import puArcade.princetonTD.towers.TowerManager;





import android.graphics.Rect;


public abstract class Game implements PlayerState, CreatureState, WaveState
{
	// Center Position
	public static final int MODE_CENTER = 0;

	// Random Position
	public static final int MODE_RANDOM = 1;

	// Position Mode
	private static final int MODE = MODE_RANDOM;

	// Game speed coefficient
	private static final double STEP_COEFF_SPEED = 0.5;

	// Max speed coefficient
	private static final double MAX_COEFF_SPEED = 5.0;

	// Min speed coefficient
	private static final double MIN_COEFF_SPEED = 0.1;

	// Playing map
	protected Map map;

	// Set of teams
	protected ArrayList<Team> teams = new ArrayList<Team>();

	// Tower manager
	protected TowerManager towerManager;

	// Creature manager
	protected CreatureManager creatureManager;

	// Animation manager
	protected AnimationManager animationManager;

	// Pause
	protected boolean pause;

	// Current wave index
	protected int indexCurrentWave = 1;

	// Wave of creatures
	protected CreatureWave currentWave;


	// Initialized?
	protected boolean isInitialized;

	// Started?
	private boolean isStarted;

	// Finished?
	protected boolean isFinished;

	// Destroyed?
	protected boolean isDestroyed;

	// Game State
	protected GameState gs;

	// Player
	protected Player player;

	// Speed coefficient
	private double coeffSpeed;

	// CONSTRUCTOR
	public Game()
	{
		towerManager		= new TowerManager(this);
		creatureManager		= new CreatureManager(this);
		animationManager	= new AnimationManager(this);
	}

	// Initializer
	synchronized public void initialize()
	{
		if(map == null)
			throw new IllegalStateException("No map");

		if(teams.size() == 0)
			throw new IllegalStateException("No team");

		// attributes
		indexCurrentWave	= 1;
		pause				= false;
		isStarted           = false;
		isFinished	        = false;
		isDestroyed         = false;
		currentWave         = map.getCreatureWave(indexCurrentWave);
		coeffSpeed          = 1.0;

		// initialize default values
		for(Team team : teams)
		{
			// initialize lives
			team.setLives(map.getLives());

			// initialize players and gold
			for(Player p : team.getPlayers())
			{
				p.setScore(0);
				p.setGold(map.getGold());
			}
		}  

		isInitialized = true;

		if(gs != null)
			gs.initialize();
	}

	public void reset()
	{
		map.reset();

		isInitialized = false;
		isStarted = false;

		initialize();

		// stop managers
		towerManager.stopTowers();
		creatureManager.stopCreatures();
		animationManager.stopAnimations();

		towerManager		= new TowerManager(this);
		creatureManager		= new CreatureManager(this);
		animationManager	= new AnimationManager(this);



		// add players
		for(Team t : teams)
		{
			t.setLives(map.getLives());

			for(Player p : t.getPlayers())    
			{
				p.setGold(map.getGold());
				p.setScore(0);
			}
		}
	}

	// Start
	public void start()
	{
		if(map == null)
			throw new IllegalStateException("No map");

		if(!isInitialized)
			throw new IllegalStateException("Game not initialized");

		if(isStarted)
			throw new IllegalStateException("Game already started");

		// start managers
		towerManager.start();
		creatureManager.start();
		animationManager.start();

		isStarted = true;

		// notification
		if(gs != null)
			gs.startGame();
	}

	// Place tower
	public void addTower(Tower tower) throws NoMoneyException, InaccessibleZoneException, BlockedPathException
	{
		// valid tower?
		if (tower == null)
			throw new IllegalArgumentException("Invalid tower!");

		// enough money?
		if(!canBuyTower(tower))    
			throw new NoMoneyException("Not enough money!");

		// valid area?
		if (!canPlaceTower(tower))
			throw new InaccessibleZoneException("Inaccessible area!");

		// is path blocked
		if (map.blockedPath(tower))
			throw new BlockedPathException("Path blocked!");

		// deactivate tower zone
		map.deactivateZone(new Rect(tower.x(),tower.y(),tower.x()+tower.width(),tower.y()+tower.height()), true);

		// add tower
		towerManager.addTower(tower);

		// update game
		tower.setGame(this);

		// activate tower
		tower.start();

		// gold transaction
		tower.getOwner().setGold(
				tower.getOwner().getGold() - tower.getPrice());

		if(gs != null)
			gs.addTower(tower);
	}


	// Sell tower
	public void sellTower(Tower tower) throws UnauthorizedException
	{
		towerManager.removeTower(tower);

		tower.getOwner().setGold(
				tower.getOwner().getGold() + tower.getSellPrice());

		if(gs != null)
			gs.sellTower(tower);
	}


	// Upgrade tower
	public void upgradeTower(Tower tower) throws MaxLevelException, NoMoneyException, UnauthorizedException
	{
		if(!tower.canUpgrade())
			throw new MaxLevelException("Max level reached!");

		if(tower.getOwner().getGold() < tower.getPrice())
			throw new NoMoneyException("Not enough money!");

		tower.getOwner().setGold(tower.getOwner().getGold() - tower.getPrice());

		tower.upgrade();

		if(gs != null)
			gs.upgradeTower(tower);
	}

	// Start new wave of creatures
	public void launchNewWave(Player player, Team team)
	{
		currentWave = map.getCreatureWave(indexCurrentWave);

		nextWave();

		creatureManager.launchWave(currentWave, player, team, this, this);
		
		currentWave = map.getCreatureWave(indexCurrentWave);
	}
	
	// Launch wave
	public void launchWave(CreatureWave creatureWave)
	{
		if(gs != null)
			gs.launchWave(creatureWave); 
	}

	// Launch Wave
	public void launchWave(Player player, Team team, CreatureWave wave) throws NoMoneyException
	{ 
		creatureManager.launchWave(wave, player, team, this, this);
	}

	// is finished?
	public boolean isFinished()
	{
		return isFinished;
	}


	// end
	public void end()
	{
		if(!isFinished)
		{
			isFinished = true;

			stopAll();

			Team winner = null;
			int maxScore = -1;

			// FIXME equal scores !

			// teams involved
			ArrayList<Team> teamsInGame = new ArrayList<Team>();
			for(Team team : teams)
				if(!team.hasLost())
				{
					// select winning team
					if(team.getScore() > maxScore)
					{
						winner = team;
						maxScore = team.getScore();
					}

					teamsInGame.add(team);
				}

			if(gs != null)
				gs.end(new GameResult(winner)); // TODO check
		}
	}

	// Set map
	public void setMap(Map map) throws IllegalArgumentException
	{
		teams = map.getTeams();

		this.map  = map;  
	}

	// Return map
	public Map getMap()
	{
		if(map == null)
			throw new NullPointerException("No map!");

		return map;
	}

	// Get creatures
	public Vector<Creature> getCreatures()
	{
		return creatureManager.getCreatures();
	}

	// Stop all
	protected void stopAll()
	{
		towerManager.stopTowers();
		creatureManager.stopCreatures();
		animationManager.stopAnimations();
	}

	// Pause
	public boolean togglePause()
	{
		if(pause)
		{
			towerManager.unpause();
			creatureManager.unpause();
			animationManager.unpause();
		}
		else
		{
			towerManager.pause();
			creatureManager.pause();
			animationManager.pause();
		}

		return pause = !pause;  
	}

	// is paused?
	public boolean isPaused()
	{
		return pause;
	}

	// return teams
	public ArrayList<Team> getTeams()
	{
		return teams;
	}

	// return players
	public ArrayList<Player> getPlayers()
	{
		ArrayList<Player> players = new ArrayList<Player>();

		for(Team t : teams)
			for(Player p : t.getPlayers())
				players.add(p);

		return players;
	}

	// Add player
	public void addPlayer(Player player) throws GameInProgressException, GameFullException
	{
		if(isStarted)
			throw new GameInProgressException("Game has started");

		for(int i=0;i<teams.size();i++)
		{
			try
			{              
				teams.get(i).addPlayer(player);

				player.setPlayerState((PlayerState) this);

				if(gs != null)
					gs.addPlayer(player);

				return;
			}
			catch (GameFullException e)
			{
				// ?
			}
		}

		throw new GameFullException("Game full");
	}

	// set GameState
	public void setGameState(GameState gs)
	{
		this.gs = gs;
	}

	// return main player
	public Player getMainPlayer()
	{ 
		return player;
	}

	// set main player
	public void setMainPlayer(Player player)
	{
		this.player = player;

		player.setPlayerState((PlayerState) this);
	}

	public void damagedCreature(Creature creature)
	{
		if(gs != null)
			gs.damagedCreature(creature);

	}

	synchronized public void killedCreature(Creature creature, Player player)
	{
		player.setGold(player.getGold() + creature.getReward());

		player.setScore(player.getScore() + creature.getReward());

		if(gs != null)
			gs.killedCreature(creature,player);
	}

	synchronized public void reachedCreature(Creature creature)
	{
		Team team = creature.getTargetTeam();

		if(!team.hasLost())
		{
			team.loseLife();

			if(gs != null)
			{
				gs.reachedCreature(creature);

				// FIXME IMPORTANT
				for(Player player : team.getPlayers())
					gs.updatePlayer(player);
			}

			if(team.hasLost())
			{
				if(gs != null)
					gs.lostTeam(team);

				int remaining = 0;
				for(Team tmp : teams)
					if(!tmp.hasLost())
						remaining++;

				if(remaining <= 1)
					end();
			}
		} 
	}

	public void updatePlayer(Player player)
	{
		if(gs != null)
			gs.updatePlayer(player);
	}

	// Can place tower
	public boolean canPlaceTower(Tower tower)
	{
		return towerManager.canPlaceTower(tower);
	}

	// Can buy tower
	public boolean canBuyTower(Tower tower)
	{  
		return towerManager.canBuyTower(tower);
	}

	// return towers
	public Vector<Tower> getTowers()
	{
		return towerManager.getTowers();
	}

	// return creatures in circle
	public Vector<Creature> getCreaturesInCircle(int centerX, int centreY, int radius)
	{
		return creatureManager.getCreaturesInCircle(centerX, centreY, radius);
	}

	// add creature
	public void addCreature(Creature creature)
	{
		if(gs != null)
			gs.addCreature(creature);
	}

	// add animation
	public void addAnimation(Animation animation)
	{
		animationManager.addAnimation(animation);

		if(gs != null)
			gs.addAnimation(animation);
	}

	// draw animations
	//	public void drawAnimations(Graphics2D g2, int height)
	//	{
	//		animationManager.drawAnimations(g2, height);
	//	}

	// get player
	public Player getPlayer(int idPlayer)
	{
		ArrayList<Player> players = getPlayers();

		for(Player player : players)
			if(player.getId() == idPlayer)
				return player;

		return null;
	}

	// get team
	public Team getTeam(int idTeam)
	{
		for(Team team : teams)
			if(team.getId() == idTeam)
				return team;

		return null;
	}

	// get Player Loaction
	public PlayerLocation getPlayerLocation(int idLocation)
	{
		for(Team team : teams)
			for(PlayerLocation pl : team.getPlayerLocations())
				if(pl.getId() == idLocation)
					return pl;

		return null;
	}

	// get Tower
	public Tower getTower(int idTower)
	{
		for (Tower tower : getTowers())
			if (tower.getId() == idTower)
				return tower;

		return null;
	}

	// get Creature
	public Creature getCreature(int idCreature)
	{
		return creatureManager.getCreature(idCreature);
	}

	// get current wave
	public CreatureWave getWave()
	{
		return currentWave;
	}
	
	// next non-empty team
	public Team getNextTeam(Team team)
	{
		int i = (teams.indexOf(team)+1) % teams.size();

		while(teams.get(i).getPlayers().size() == 0 || teams.get(i).hasLost())
			i = ++i % teams.size();

		return teams.get(i);
	}

	// is initialized?
	public boolean isInitialized()
	{
		return isInitialized;
	}

	// is started?
	public boolean isStarted()
	{
		return isStarted;
	}

	// return current wave number
	public int getCurrentWave()
	{
		return indexCurrentWave;
	}

	// move onto next wave
	public void nextWave()
	{
		indexCurrentWave++;
	}

	// destroy
	public void destroy()
	{
		isDestroyed = true;

		towerManager.destroy();
		creatureManager.destroy();
		animationManager.destroy();
	}

	// is destroyed?
	public boolean isDestroyed()
	{
		return isDestroyed;
	}

	// return speed coefficient
	public double getCoeffSpeed()
	{
		return coeffSpeed;
	}

	// increment speed coefficient
	public synchronized double increaseSpeed()
	{
		if(coeffSpeed + STEP_COEFF_SPEED <= MAX_COEFF_SPEED)
		{    
			coeffSpeed += STEP_COEFF_SPEED;

			if(gs != null)
				gs.modifyCoeffSpeed(coeffSpeed);
		}

		return coeffSpeed;
	}

	// decrement speed coefficient
	synchronized public double decreaseSpeed()
	{
		if(coeffSpeed - STEP_COEFF_SPEED >= MIN_COEFF_SPEED)
		{ 
			coeffSpeed -= STEP_COEFF_SPEED;

			if(gs != null)
				gs.modifyCoeffSpeed(coeffSpeed);
		}    
		return coeffSpeed;
	}

	// set speed coefficient
	public void setCoeffSpeed(double value)
	{
		if(coeffSpeed - STEP_COEFF_SPEED < MIN_COEFF_SPEED
				&& coeffSpeed + STEP_COEFF_SPEED > MAX_COEFF_SPEED)
			throw new IllegalArgumentException(
					"Cannot modify speed");

		coeffSpeed = value;

		if(gs != null)
			gs.modifyCoeffSpeed(coeffSpeed);
	}

	// return positioning mode
	public int getMode()
	{
		return MODE;
	}

	// add team
	public void addTeam(Team team)
	{
		teams.add(team);
	}

	// remove team
	public void removeTeam(Team team)
	{
		teams.remove(team);
	}

}
