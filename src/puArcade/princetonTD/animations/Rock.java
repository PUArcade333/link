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

package puArcade.princetonTD.animations;

import puArcade.princetonTD.creatures.Creature;
import puArcade.princetonTD.game.Game;
import puArcade.princetonTD.towers.Tower;

public class Rock extends Attack {
	
	private static final int DIAMETER = 10;
    private static final int DIAMETER_MAX = 20;
    private static final String IMAGE;

    private double speed = 0.1; // px / ms
    private double distCenter = 0;

    private double xCenter, yCenter;

    private double distanceMax;
    private double distanceMaxInit;
    
    // image for attack
    static
    {
        IMAGE = "drawable/rock";
    }
    
    // constructor
    public Rock(Game game, Tower attacker, Creature target, long damage,
            double radius)
    {
        super((int) attacker.centerX(), (int) attacker.centerY(), game,
                attacker, target);

        this.damage = damage;
        this.radius = radius;
        
        this.distanceMaxInit = distance();
    }
    
    // Android does not allow subclasses to draw, so draw method haS been removed
    
//    @Override
//    public void draw(Graphics2D g2)
//    {
//        double xAttacker = attacker.centerX();
//        double yAttacker = attacker.centerY();
//
//        double angle = Math.atan2(target.centerY() - yAttacker, target
//                .centerX()
//                - xAttacker);
//
//        xCenter = Math.cos(angle) * distCenter + xAttacker; // x
//        yCenter = Math.sin(angle) * distCenter + yAttacker; // y
//
//        
//        int diameter = 0;
//        if(distanceMaxInit > 100.0)
//        {
//            double p = distCenter / (distanceMax/2.0);
//            
//            if(distCenter > distanceMax / 2.0)
//                p = 1 - (p - 1);
//            
//            diameter = (int) (p * DIAMETER_MAX + DIAMETER);
//        }
//        else
//        {
//            diameter = DIAMETER;
//        }
//       
//
//        
//        g2.drawImage(IMAGE, (int) xCenter - diameter / 2,
//                (int) yCenter - diameter / 2, 
//                diameter,
//                diameter, null);
//    }
    
    // execute attack
    @Override
    public void animate(long t)
    {
        if(!finished)
        {
            distCenter += t * speed;
           
            distanceMax = distance();

            if (distCenter >= distanceMax)
            {
                endAttack();
                finished = true;
    
                targets();
                finished = true;
            }
        }
    }
    
    private double distance()
    {
        double diffX = target.centerX() - attacker.centerX();
        double diffY = target.centerY() - attacker.centerY();
        return Math.sqrt(diffX * diffX + diffY * diffY);   
    }

}
