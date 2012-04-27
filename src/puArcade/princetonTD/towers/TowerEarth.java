package puArcade.princetonTD.towers;

import puArcade.princetonTD.animations.Rock;
import puArcade.princetonTD.creatures.Creature;
import android.graphics.Color;

public class TowerEarth extends Tower {
	
	public static final int COLOR;
	public static final String IMAGE;
	public static final String ICON;
	public static final int MAX_LEVEL = 5;
	public static final int PRICE = 250;
	private static final double RADIUS = 30;
	private static final String DESCRIPTION = ""; 
	
	static
	{
		COLOR 	= Color.GREEN;
		IMAGE   = "drawable/towerearth";
		ICON    = "drawable/icontowerearth";
	}
	
	public TowerEarth()
	{
		super( 	0,
				0,
				20,
				20,
				COLOR,
				"Earth Tower",
				PRICE,
				400,
				150,
				0.5,
				Tower.TYPE_LAND,
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
		game.addAnimation(new Rock(game,this,creature,damage,RADIUS));
	}

	@Override
	public Tower copy() {
		return new TowerEarth();
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
