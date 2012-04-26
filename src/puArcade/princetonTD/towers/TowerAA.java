package puArcade.princetonTD.towers;

import puArcade.princetonTD.animations.Arrow;
import puArcade.princetonTD.creatures.Creature;
import android.graphics.Color;

public class TowerAA extends Tower {
	
	public static final int COLOR;
	public static final String IMAGE;
	public static final String ICON;
	public static final int MAX_LEVEL = 5;
	public static final int PRICE = 30;
	private static final String DESCRIPTION = ""; 
	
	static
	{
		COLOR 	= Color.BLUE;
		IMAGE   = "drawable/toweraa";
		ICON    = "drawable/icontoweraa";
	}
	
	public TowerAA()
	{
		super( 	0,
				0,
				20,
				20,
				COLOR,
				"Anti-Air Tower",
				PRICE,
				40,
				50,
				3,
				Tower.TYPE_AIR,
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
		game.addAnimation(new Arrow(game,this,creature,damage));
	}

	@Override
	public Tower copy() {
		return new TowerAA();
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
