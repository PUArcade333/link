package puArcade.princetonTD.game;

import puArcade.princetonTD.animations.Animation;
import puArcade.princetonTD.creatures.Creature;
import puArcade.princetonTD.creatures.CreatureWave;
import puArcade.princetonTD.players.Player;
import puArcade.princetonTD.players.Team;
import puArcade.princetonTD.towers.Tower;

public interface GameState {
	
	// Game Instance
	
	public void initialize();
	
	public void startGame();
	
	public void end(GameResult gameResult);
	
	public void modifyCoeffSpeed(double coeffSpeed);
	
	// Player
	
	public void addPlayer(Player player);
	
	public void updatePlayer(Player player);
	
	public void lostTeam(Team team);
	
	// Towers
	
	public void addTower(Tower tower);
	
	public void sellTower(Tower tower);
	
	public void upgradeTower(Tower tower);
	
	// Waves
	
	public void launchWave(CreatureWave wave);
	
	// Creatures
	
	public void addCreature(Creature creature);
	
	public void damagedCreature(Creature creature);
	
	public void killedCreature(Creature creature, Player player);
	
	public void reachedCreature(Creature creature);
	
	// Animations
	
	public void addAnimation(Animation animation);
	
	public void endAnimation(Animation animation);
	
}
