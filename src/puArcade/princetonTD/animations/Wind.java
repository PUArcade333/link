package puArcade.princetonTD.animations;


import java.util.ArrayList;
import java.util.Vector;

import puArcade.princetonTD.creatures.Creature;
import puArcade.princetonTD.game.Game;
import puArcade.princetonTD.towers.Tower;



public class Wind extends Attack {

	private static final int DIAMETER = 20;
	private static final String IMAGE;

	private ArrayList<Creature> touched = new ArrayList<Creature>();

	private double speed = 0.1; // px / ms
	private final double MAX_DISTANCE;

	private double distSource = 0;
	private final double ANGLE;
	private float alpha;

	static
	{
		IMAGE = "drawable/wind.png";
	}


	public Wind(Game game, Tower attacker, Creature cible, long damage)
	{
		super((int) attacker.centerX(), (int) attacker.centerY(), game,
				attacker, cible);

		this.damage = damage;

		ANGLE = Math.atan2(cible.centerY() - attacker.centerY(), 
				cible.centerX() - attacker.centerX());

		MAX_DISTANCE = attacker.getRange();

	}


//	@Override
//	public void draw(Graphics2D g2)
//	{ 
//		AffineTransform tx = new AffineTransform();
//		tx.translate((int) x, (int) y);
//		tx.rotate(ANGLE);
//		tx.translate((int) -DIAMETER / 2.0, (int) -DIAMETER / 2.0);
//
//		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
//		g2.drawImage(IMAGE, tx, null);
//		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));  
//	}

	@Override
	public void animate(long t)
	{
		if(!finished)
		{
			if(distSource >= MAX_DISTANCE)
			{
				endAttack();
				finished = true;
			}
			else
			{
				distSource += t * speed;

				alpha = (float) (1.f - distSource / MAX_DISTANCE);

				if(alpha < 0.0)
					alpha = 0.0f;

				x = (int) (Math.cos(ANGLE) * distSource + attacker.centerX()); // x
				y = (int) (Math.sin(ANGLE) * distSource + attacker.centerY()); // y


				Vector<Creature> creatures = game.getCreaturesInCircle((int)x,(int)y,DIAMETER);
				Creature creature;
				for(int i=0;i<creatures.size();i++)
				{
					creature = creatures.get(i);

					if(creature.getType() == Creature.TYPE_AIR && !touched.contains(creature))
					{
						creature.damaged((long)(damage * alpha), attacker.getOwner());
						touched.add(creature);
					}
				}
			}
		}
	}

}
