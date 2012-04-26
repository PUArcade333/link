package puArcade.princetonTD.creatures;


public class Elephant extends Creature {

	private static final String IMAGE = "drawable/elephant";

	public Elephant(long healthMax, int reward, double speed)
	{
		this(0, 0, healthMax, reward, speed);
	}
	
	public Elephant(int x, int y, long healthMax, int reward, double speed)
	{
		super(x, y, 24, 24, healthMax, reward, speed,
				Creature.TYPE_LAND, IMAGE, "Elephant");
	}

	public Creature copy()
	{
		return new Elephant(this.x(),this.y(),getHealthMax(),getReward(),getSpeed());
	}

}
