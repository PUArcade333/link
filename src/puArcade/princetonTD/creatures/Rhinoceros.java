package puArcade.princetonTD.creatures;


public class Rhinoceros extends Creature {

	private static final String IMAGE = "drawable/rhinoceros";

	public Rhinoceros(long healthMax, int reward, double speed)
	{
		this(0, 0, healthMax, reward,speed);
	}

	public Rhinoceros(int x, int y, long healthMax, int reward, double speed)
	{
		super(x, y, 24, 24, healthMax, reward, speed, 
				Creature.TYPE_LAND, IMAGE, "Rhinoceros");
	}

	public Creature copy()
	{
		return new Rhinoceros(this.x(),this.y(),getHealthMax(),getReward(),getSpeed());
	}

}
