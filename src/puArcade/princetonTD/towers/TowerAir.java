package puArcade.princetonTD.towers;

import puArcade.princetonTD.animations.Wind;
import puArcade.princetonTD.creatures.Creature;
import android.graphics.Color;

public class TowerAir extends Tower {
	
	public static final int COLOR;
	public static final String IMAGE;
	public static final String ICON;
	public static final int MAX_LEVEL = 5;
	public static final int PRICE = 150;
	private static final String DESCRIPTION = ""; 
	
	static
	{
		COLOR 	= Color.WHITE;
		IMAGE   = "drawable/towerair";
		ICON    = "drawable/icontowerair";
	}
	
	public TowerAir()
	{
		super( 	0,
				0,
				20,
				20,
				COLOR,
				"Air Tower",
				PRICE,
				200,
				100,
				2,
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
		game.addAnimation(new Wind(game,this,creature,damage));
	}

	@Override
	public Tower copy() {
		return new TowerAir();
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
