package puArcade.princetonTD.creatures;


public class BlackSheep extends Creature {

	private static final String IMAGE = "drawable/blacksheep";

	public BlackSheep(long healthMax, int reward, double speed)
	{
		this(0, 0, healthMax, reward,speed);
	}

	public BlackSheep(int x, int y, long healthMax, int reward, double speed)
	{
		super(x, y, 16, 16, healthMax, reward, speed,
				Creature.TYPE_LAND, IMAGE, "Sheep");
	}

	public Creature copy()
	{
		return new BlackSheep(this.x(),this.y(),getHealthMax(),getReward(),getSpeed());
	}

}
