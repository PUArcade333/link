package puArcade.princetonTD.towers;

import puArcade.princetonTD.animations.Electricity;
import puArcade.princetonTD.creatures.Creature;
import android.graphics.Color;

public class TowerElectric extends Tower {
	
	public static final int COLOR;
	public static final String IMAGE;
	public static final String ICON;
	public static final int MAX_LEVEL = 5;
	public static final int PRICE = 120;
	private static final String DESCRIPTION = ""; 
	
	static
	{
		COLOR 	= Color.MAGENTA;
		IMAGE   = "drawable/towerelect";
		ICON    = "drawable/icontowerelect";
	}
	
	public TowerElectric()
	{
		super( 	0,
				0,
				20,
				20,
				COLOR,
				"Electricity Tower",
				PRICE,
				120,
				70,
				1,
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
		game.addAnimation(new Electricity(game,this,creature,damage));
	}

	@Override
	public Tower copy() {
		return new TowerElectric();
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
