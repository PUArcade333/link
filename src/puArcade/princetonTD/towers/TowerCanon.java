package puArcade.princetonTD.towers;

import puArcade.princetonTD.animations.Canonball;
import puArcade.princetonTD.creatures.Creature;
import android.graphics.Color;

public class TowerCanon extends Tower {

	public static final int COLOR;
	public static final String IMAGE;
	public static final String ICON;
	public static final int MAX_LEVEL = 5;
	private static final double RADIUS = 30.0;
	public static final int PRICE = 15;
	private static final String DESCRIPTION = ""; 
	
	static
	{
		COLOR 	= Color.BLACK;
		IMAGE   = "drawable/towercanon";
		ICON    = "drawable/icontowercanon";
	}
	
	public TowerCanon()
	{
		super( 	0,
				0,
				20,
				20,
				COLOR,
				"Canon Tower",
				PRICE,
				18,
				40,
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
		angle = Math.PI/2+Math.atan2(creature.centerY() - centerY(), creature.centerX() - centerX());
		creature.damaged(damage, owner);
        game.addAnimation(new Canonball(game,this,creature,damage,RADIUS));
	}

	@Override
	public Tower copy() {
		return new TowerCanon();
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
