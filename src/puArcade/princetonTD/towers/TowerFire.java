package puArcade.princetonTD.towers;

import puArcade.princetonTD.animations.Fireball;
import puArcade.princetonTD.creatures.Creature;
import android.graphics.Color;

public class TowerFire extends Tower {
	
	public static final int COLOR;
	public static final String IMAGE;
	public static final String ICON;
	public static final int MAX_LEVEL = 5;
	private static final double RADIUS = 20.0;
	public static final int PRICE = 120;
	private static final String DESCRIPTION = ""; 
	
	static
	{
		COLOR 	= Color.YELLOW;
		IMAGE   = "drawable/towerfire";
		ICON    = "drawable/icontowerfire";
	}
	
	public TowerFire()
	{
		super( 	0,
				0,
				20,
				20,
				COLOR,
				"Fire Tower",
				PRICE,
				10,
				40,
				10,
				Tower.TYPE_LAND_AND_AIR,
				IMAGE,
				ICON);                 

		description = DESCRIPTION;
	}
	
	@Override
	public void upgrade() {
		if(canUpgrade())
		{
			priceTotal += price;

			price *= 2;

			damage = nextDamage();

			range = nextRange();

			setRate(nextRate());

			level++;
		}
	}

	@Override
	protected void attack(Creature creature) {
		creature.damaged(damage, owner);
		game.addAnimation(new Fireball(game,this,creature,damage,RADIUS));
	}

	@Override
	public Tower copy() {
		return new TowerFire();
	}

	@Override
	public boolean canUpgrade() {
		return level < MAX_LEVEL;
	}

	@Override
	public long nextDamage() {
		return (long) (damage * 1.5);
	}

	@Override
	public double nextRange() {
		return range + 10;
	}

	@Override
	public double nextRate() {
		return getRate() * 1.2;
	}

}
