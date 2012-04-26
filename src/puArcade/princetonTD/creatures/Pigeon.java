package puArcade.princetonTD.creatures;


public class Pigeon extends Creature {

	private static final String IMAGE = "drawable/pigeon";

	public Pigeon(long healthMax, int reward, double speed)
	{
		this(0, 0, healthMax, reward,speed);
	}

	public Pigeon(int x, int y, long healthMax, int reward, double speed)
	{
		super(x, y, 16, 16, healthMax, reward, speed, 
				Creature.TYPE_AIR, IMAGE, "Pigeon");
	}

	public Creature copy()
	{
		return new Pigeon(this.x(),this.y(),getHealthMax(),getReward(),getSpeed());
	}

}
