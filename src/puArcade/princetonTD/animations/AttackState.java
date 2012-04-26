package puArcade.princetonTD.animations;

import puArcade.princetonTD.creatures.Creature;
import puArcade.princetonTD.towers.Tower;

public interface AttackState
{
	public void endAttack(Tower attacker, Creature target);
}
