package puArcade.princetonTD.creatures;


public class Eagle extends Creature {

	private static final String IMAGE = "drawable/eagle";

	public Eagle(long healthMax, int reward, double speed)
	{
		this(0, 0, healthMax, reward,speed);
	}

	public Eagle(int x, int y, long healthMax, int reward, double speed)
	{
		super(x, y, 16, 16, healthMax, reward, speed, 
				Creature.TYPE_AIR, IMAGE, "Eagle");
	}

	public Creature copy()
	{
		return new Eagle(this.x(),this.y(),getHealthMax(),getReward(),getSpeed());
	}


}
