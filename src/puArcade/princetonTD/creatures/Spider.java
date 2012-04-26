package puArcade.princetonTD.creatures;


public class Spider extends Creature {

	private static final String IMAGE = "drawable/spider";

	public Spider(long healthMax, int reward, double speed)
	{
		this(0, 0, healthMax, reward,speed);
	}

	public Spider(int x, int y, long healthMax, int reward, double speed)
	{
		super(x, y, 16, 16, healthMax, reward, speed,
				Creature.TYPE_LAND, IMAGE, "Spider");
	}

	public Creature copy()
	{
		return new Spider(this.x(),this.y(),getHealthMax(),getReward(),getSpeed());
	}

}
