package puArcade.princetonTD.creatures;


public class Sheep extends Creature {

	private static final String IMAGE = "drawable/sheep";

	public Sheep(long healthMax, int reward, double speed)
	{
		this(0, 0, healthMax, reward, speed);
	}

	public Sheep(int x, int y, long healthMax, int reward, double speed)
	{
		super(x, y, 16, 16, healthMax, reward, speed,
				Creature.TYPE_LAND, IMAGE, "Sheep");
	}

	public Creature copy()
	{
		return new Sheep(this.x(),this.y(),getHealthMax(),getReward(),getSpeed());
	}

}
