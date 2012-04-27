package puArcade.princetonTD.creatures;

import puArcade.princetonTD.players.Player;

public interface CreatureState {
	
	// Creature killed?
	void killedCreature(Creature creature, Player tueur);
    
    // Creature damaged?
    void damagedCreature(Creature creature);
    
    // Creature arrived? 
    void reachedCreature(Creature creature);

}
