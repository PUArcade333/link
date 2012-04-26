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
    
    static
    {
        IMAGE = "drawable/rock.png";
    }

    public Rock(Game game, Tower attacker, Creature target, long damage,
            double radius)
    {
        super((int) attacker.centerX(), (int) attacker.centerY(), game,
                attacker, target);

        this.damage = damage;
        this.radius = radius;
        
        this.distanceMaxInit = distance();
    }

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
