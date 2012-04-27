package puArcade.princetonTD.maps;

import puArcade.princetonTD.game.Game;
import puArcade.princetonTD.game.Mode;
import puArcade.princetonTD.players.PlayerLocation;
import puArcade.princetonTD.players.Team;
import android.graphics.Color;
import android.graphics.Rect;

public class Default extends Map
{
	public final static String BG_IMAGE;
	public final static String MENU;
	public final static String NAME = "defaultmap";

	static
	{
		MENU    = "drawable/defaultmapmenu";
		BG_IMAGE = "drawable/defaultmap";
	}

	public Default (Game game) 
	{
		super(  game,
				480,  // width
				500,  // height
				100,  // gold
				20,   // lives
				0,    // x offset
				-40,  // y offset
				480,  // grid width
				540,  // grid height
				Mode.MODE_SOLO, // mode
				BG_IMAGE, // background image
				NAME  // name
				);

		opacity = 0.f;

		Team e = new Team(1,"Default team",Color.BLACK);
		e.addStartZone(new Rect(110, -40, 190, -20));
		e.setEndZone(new Rect(250, 0, 290, 40));
		e.addPlayerLocation(new PlayerLocation(1,new Rect(0,0,480,500)));
		teams.add(e);

		addWall(new Rect(0, -40, 100, 20));
		addWall(new Rect(0, 20, 20, 500));
		addWall(new Rect(20, 480, 460, 500));
		addWall(new Rect(460, 20, 480, 500));
		addWall(new Rect(320, -40, 480, 20));
		addWall(new Rect(200, -40, 220, 100));
		addWall(new Rect(120, 100, 360, 120));
		addWall(new Rect(120, 120, 140, 140));
		addWall(new Rect(340, 120, 360, 380));
		addWall(new Rect(120, 360, 340, 380));
		addWall(new Rect(20, 240, 240, 260));
		addWall(new Rect(220, 220, 240, 240));
	}
}
