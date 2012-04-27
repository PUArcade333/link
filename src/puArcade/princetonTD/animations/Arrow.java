package puArcade.princetonTD.animations;

import puArcade.princetonTD.creatures.Creature;
import puArcade.princetonTD.game.Game;
import puArcade.princetonTD.towers.Tower;
import android.graphics.Color;
import android.graphics.Point;

public class Arrow extends Attack {

	private static final int LENGTH   = 10;
    private static final int COLOR  = Color.RED;
    
    private double speed = 0.2; // px / ms
    private double distHeadTower;
    
    private double xHead, yHead;
    private double xTail, yTail;
    

    public Arrow(Game game, Tower attacker, Creature target, long damage)
    {
        super((int) attacker.centerX(),(int) attacker.centerY(), game, attacker, target);
        
        this.damage = damage;
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
//        xHead = Math.cos(angle)*distHeadTower + xAttacker; // x
//        yHead = Math.sin(angle)*distHeadTower + yAttacker; // y
//        
//        xTail = Math.cos(angle)*(distHeadTower - LENGTH) + xAttacker; // x
//        yTail = Math.sin(angle)*(distHeadTower - LENGTH) + yAttacker; // y
//        
//        
//        
//        
//        int widthPoint = 2;
//        
//        Polygon p = new Polygon();
//       
//        Point vector = new Point((int) (xHead - xTail), (int) (yHead - yTail));
//        
//       
//        Point v = new Point((int)(vector.x * 0.6), 
//                                      (int)(vector.y * 0.6));
//
//        
//        g2.setColor(COLOR);
//        g2.drawLine((int) xTail + v.x,(int) yTail + v.y,(int) xTail,(int) yTail);
//        
//        
//        
//        
//        Point vRight = new Point((int)(vector.y / Math.sqrt(vector.x*vector.x+vector.y*vector.y) * widthPoint),
//                                  (int)( -vector.x / Math.sqrt(vector.x*vector.x+vector.y*vector.y) * widthPoint));
//        
//        Point vLeft = new Point((int)(vRight.x * -1),
//                                  (int)(vRight.y * -1));
//        
//        
//        p.addPoint((int)xHead,(int)yHead);
//        p.addPoint((int)(xTail + v.x + vRight.x), (int)(yTail + v.y + vRight.y));
//        p.addPoint((int)(xTail + v.x + vLeft.x), (int)(yTail + v.y + vLeft.y));
//        
//        // draw
//        g2.setColor(Color.GRAY);
//        g2.fillPolygon(p);
//        
//    }

    @Override
    public void animate(long t)
    {
        if(!finished)
        {
            distHeadTower += t * speed;
            
            double diffX       = target.centerX() - attacker.centerX();
            double diffY       = target.centerY() - attacker.centerY();  
            double distanceMax = Math.sqrt(diffX * diffX + diffY * diffY);
            
            if (distHeadTower >= distanceMax)
            {
                endAttack();
                finished = true;
                
                targets();
                finished = true;
            }
        }
    }

}
