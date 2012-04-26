package puArcade.princetonTD.animations;

import puArcade.princetonTD.creatures.Creature;
import puArcade.princetonTD.game.Game;
import puArcade.princetonTD.towers.Tower;

public class Iceball extends Attack {

	private static final int DIAMETER     = 10;
    private static final String IMAGE;
    private static final long LENGTH_SLOW = 5000;
    
    private double speed = 0.2; // px / ms
    private double distCenter = 0;
    
    private double xCenter, yCenter;
    
    static
    {
        IMAGE = "drawable/iceball.png";
    }
    
    
    public Iceball(Game game, Tower attacker, Creature target, 
                      long damage, double coeffSlow)
    {
        super((int) attacker.centerX(),(int) attacker.centerY(), game, attacker, target);
        
        this.damage       = damage;
        this.coeffSlow    = coeffSlow;
    }

//    @Override
//    public void draw(Graphics2D g2)
//    {
//        double xAttacker = attacker.centerX();
//        double yAttacker = attacker.centerY();
//         
//        double angle = Math.atan2(
//                        target.centerY() - yAttacker,
//                        target.centerX() - xAttacker); 
//        
//        xCenter = Math.cos(angle)*distCenter + xAttacker;
//        yCenter = Math.sin(angle)*distCenter + yAttacker;
//             
//        g2.drawImage(IMAGE, 
//                    (int) xCenter - DIAMETER / 2, 
//                    (int) yCenter - DIAMETER / 2, 
//                    DIAMETER, 
//                    DIAMETER, null);
//    }

    @Override
    public void animate(long t)
    {   
        if(!finished)
        {
            distCenter += t * speed;
            
            double diffX       = target.centerX() - attacker.centerX();
            double diffY       = target.centerY() - attacker.centerY();  
            double distanceMax = Math.sqrt(diffX * diffX + diffY * diffY);
            
            if (distCenter >= distanceMax)
            {
                endAttack();
                targets();
               
                if(target.getCoeffSlow() == 0.0)
                {
                    game.addAnimation(new Ice(game,attacker,target,
                            coeffSlow,LENGTH_SLOW));
                    
                }
  
                finished = true;
            }
        }
    }

}
