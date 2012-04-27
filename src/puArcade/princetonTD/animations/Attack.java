package puArcade.princetonTD.animations;


import java.util.ArrayList;
import java.util.Enumeration;

import puArcade.princetonTD.creatures.Creature;
import puArcade.princetonTD.game.Game;
import puArcade.princetonTD.towers.Tower;


import android.graphics.Point;


abstract public class Attack extends Animation
{

    protected Creature target;
    protected Tower attacker;

    protected long damage;
    protected double radius;
    protected double coeffSlow;
   
    protected Game game;
    private ArrayList<AttackState> attackStates = new ArrayList<AttackState>();
   

    public Attack(int x, int y, Game game, Tower attacker, Creature target)
    {
        super(x, y);
        this.game       = game;
        this.attacker  	= attacker;
        this.target     = target;
    }
   
    public ArrayList<Creature> targets()
    {
        if(radius > 0.0)
            return damagedCreatures();
        else
        {
            if(target.canBeAttacked(attacker))
                target.damaged(damage,attacker.getOwner());
           
            ArrayList<Creature> a = new ArrayList<Creature>();
            a.add(target);
            return a;
        }
    }
   
    synchronized public ArrayList<Creature> damagedCreatures()
    {
        ArrayList<Creature> a = new ArrayList<Creature>();
       
        Point impact = new Point((int) target.centerX(), (int) target.centerY());
        long finalDamage;
        double distanceImpact;
       
        Enumeration<Creature> eCreatures = game.getCreatures().elements();
        Creature tmpCreature;
        while(eCreatures.hasMoreElements())
        {
            tmpCreature = eCreatures.nextElement();
           
            if(tmpCreature.canBeAttacked(attacker))
            {
                distanceImpact = Math.sqrt((tmpCreature.centerX()-impact.x)*(tmpCreature.centerX()-impact.x)+
                                           (tmpCreature.centerY()-impact.y)*(tmpCreature.centerY()-impact.y));
               
                if(distanceImpact <= radius)
                {
                    finalDamage = (long) (damage - (distanceImpact / radius * damage));
                    tmpCreature.damaged(finalDamage, attacker.getOwner());
                   
                    a.add(tmpCreature);
                }
            }
        }
       
        return a;
    }
   
    public void addAttackState(AttackState as)
    {
        attackStates.add(as);
    }
   
    protected void endAttack()
    {
        for(AttackState as : attackStates)
            as.endAttack(attacker, target);
    }
}
