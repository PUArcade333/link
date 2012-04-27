package puArcade.princetonTD.towers;

import puArcade.princetonTD.animations.Iceball;
import puArcade.princetonTD.creatures.Creature;
import android.graphics.Color;

public class TowerIce extends Tower {
	
	public static final int COLOR;
	public static final String IMAGE;
	public static final String ICON;
	public static final int MAX_LEVEL = 5;
	private static final double COEFF_SLOW = 0.4;
	public static final int PRICE = 50;
	private static final String DESCRIPTION = ""; 
	
	static
	{
		COLOR 	= Color.CYAN;
		IMAGE   = "drawable/towerice";
		ICON    = "drawable/icontowerice";
	}
	
	public TowerIce()
	{
		super( 	0,
				0,
				20,
				20,
				COLOR,
				"Ice Tower",
				PRICE,
				20,
				50,
				2,
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
		game.addAnimation(new Iceball(game,this,creature,damage,COEFF_SLOW));
	}

	@Override
	public Tower copy() {
		return new TowerIce();
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
