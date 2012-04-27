package puArcade.princetonTD.animations;

import puArcade.princetonTD.creatures.Creature;
import puArcade.princetonTD.game.Game;
import puArcade.princetonTD.towers.Tower;

public class Ice extends Attack {

	private static final String IMAGE;
	private final long LENGTH_SLOW;
	private long t = 0L;

	static
	{
		IMAGE = "drawable/ice.png";
	}

	public Ice(Game game, Tower attacker, Creature target, double coeffSlow,  long lengthSlow)
	{
		super((int)attacker.x(), (int) attacker.y(), game, attacker, target);

		target.setCoeffSlow(coeffSlow);

		LENGTH_SLOW = lengthSlow;
	}

//	@Override
//	public void draw(Graphics2D g2)
//	{
//		g2.drawImage(IMAGE, 
//				(int) target.x(), 
//				(int) target.y(), 
//				(int) target.width(), 
//				(int) target.height(), null);
//	}

	@Override
	public void animate(long t)
	{ 
		this.t += t;

		if(target.isDead())
			finished = true;
		else if(this.t > LENGTH_SLOW)
		{
			target.setCoeffSlow(0.0);
			finished = true;
		}
	}

}
