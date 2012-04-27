package puArcade.princetonTD.towers;

public class TowerType {

	private static final int TOWER_ARCHER       = 1;
	private static final int TOWER_CANON        = 2;
	private static final int TOWER_AA           = 3;
	private static final int TOWER_ICE      	= 4;
	private static final int TOWER_ELECTRIC     = 5;
	private static final int TOWER_FIRE         = 6;
	private static final int TOWER_AIR          = 7;
	private static final int TOWER_EARTH        = 8;
	private static final int N					= 8;


	public static int getTowerType(Tower tower)
	{
		if (tower instanceof TowerArcher)
			return TOWER_ARCHER;
		else if (tower instanceof TowerCanon)
			return TOWER_CANON;
		else if (tower instanceof TowerAA)
			return TOWER_AA;
		else if (tower instanceof TowerIce)
			return TOWER_ICE;
		else if (tower instanceof TowerElectric)
			return TOWER_ELECTRIC;
		else if (tower instanceof TowerFire)
			return TOWER_FIRE;
		else if (tower instanceof TowerAir)
			return TOWER_AIR;
		else if (tower instanceof TowerEarth)
			return TOWER_EARTH;

		return -1;
	}

	public static Tower getTower(int type)
			{
		switch(type)
		{
		case TOWER_ARCHER : 
			return new TowerArcher();
		case TOWER_CANON :
			return new TowerCanon();
		case TOWER_AA : 
			return new TowerAA();
		case TOWER_ICE : 
			return new TowerIce();
		case TOWER_ELECTRIC :
			return new TowerElectric();
		case TOWER_FIRE : 
			return new TowerFire();
		case TOWER_AIR :
			return new TowerAir();
		case TOWER_EARTH :
			return new TowerEarth();
		default : 
			return null;
		}
			}
	
	public static int getN()
	{
		return N;
	}

}
