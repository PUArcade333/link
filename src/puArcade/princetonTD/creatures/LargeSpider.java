package puArcade.princetonTD.creatures;


public class LargeSpider extends Creature {

	private static final String IMAGE = "drawable/largespider";
	
	public LargeSpider(long healthMax, int reward, double speed)
	{
		this(0, 0, healthMax, reward,speed);
	}

	public LargeSpider(int x, int y, long healthMax, int reward, double speed)
	{
		super(x, y, 32, 32, healthMax, reward, speed,
				Creature.TYPE_LAND, IMAGE, "Large Spider");
	}

	public Creature copy()
	{
		return new LargeSpider(this.x(),this.y(),getHealthMax(),getReward(),getSpeed());
	}

}
