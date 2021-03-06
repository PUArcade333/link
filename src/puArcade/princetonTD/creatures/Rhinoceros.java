/*
  Copyright (C) 2010 Aurelien Da Campo

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

/*
  Unless stated otherwise, all code below is from said above open 
  source project. Code variables have been translated from French to
  English to facilitate development. Everything else has been left intact
  from the original source.
  
  Modified portions are further commented detailing changes made.
*/

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
