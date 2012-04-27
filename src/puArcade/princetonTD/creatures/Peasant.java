package puArcade.princetonTD.creatures;


public class Peasant extends Creature {

	private static final String IMAGE = "drawable/peasant";

	public Peasant(long healthMax, int reward, double speed)
	{
		this(0, 0, healthMax, reward,speed);
	}
	
	public Peasant(int x, int y, long healthMax, int reward, double speed)
	{
		super(x, y, 20, 20, healthMax, reward, speed,
				Creature.TYPE_LAND, IMAGE, "Peasant");
	}

	public Creature copy()
	{
		return new Peasant(this.x(),this.y(),getHealthMax(),getReward(),getSpeed());
	}

}
