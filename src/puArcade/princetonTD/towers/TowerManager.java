package puArcade.princetonTD.towers;


import java.util.Enumeration;
import java.util.Vector;

import puArcade.princetonTD.creatures.Creature;
import puArcade.princetonTD.game.Game;


import android.graphics.Rect;

public class TowerManager implements Runnable {
	
	private static final long WAIT_TIME = 50;
    private Vector<Tower> towers = new Vector<Tower>();
    private boolean inManagement;
    private Game game;
    private boolean paused = false;
    private Object pause = new Object();
    
    // Constructor
    public TowerManager(Game game)
    {
        this.game = game; 
    }
    
    // start
    public void start()
    {
        Thread thread = new Thread(this);
        thread.start();
    }
    
    // remove a tower
    public void removeTower(Tower tower)
    {
        if (tower == null)
            throw new IllegalArgumentException("Invalid tower!");

        tower.stop();

        towers.remove(tower);
        
        // reactivate tower zone
        game.getMap().activateZone(new Rect(tower.x(),tower.y(),tower.x()+tower.width(),tower.y()+tower.height()), true);
    }
    
    // add tower
    public void addTower(Tower tower)
    {
        towers.add(tower);
    }
    
    public void run()
    {
        inManagement = true;
        
        while(inManagement)
        {
            Tower tower;
            Enumeration<Tower> eTowers = towers.elements();
            while(eTowers.hasMoreElements())
            {
                tower = eTowers.nextElement();
  
                if(tower.isActive())
                    tower.action((long)(WAIT_TIME*game.getCoeffSpeed())); 
            }
          
            // pause
            try{
                synchronized (pause)
                {
                    if(paused)
                        pause.wait();
                } 
            } 
            catch (InterruptedException e1){
                e1.printStackTrace();
            }
            
            try{
                 Thread.sleep(WAIT_TIME);
            } 
            catch (InterruptedException e){
                 e.printStackTrace();
            }
        }
    }
    
    // stop management
    public void stopTowers()
    {
        inManagement = false;
    }
    
    // return towers managed
    @SuppressWarnings("unchecked")
    public Vector<Tower> getTowers()
    {
        return (Vector<Tower>) towers.clone();
    }

    // pause
    public void pause()
    {
        paused = true;
    }
    
    // unpause
    public void unpause()
    { 
        synchronized (pause)
        {
            paused = false;
            pause.notify(); 
        }
    }

    // Can place tower?
    public boolean canPlaceTower(Tower tower)
    {
        if (tower == null)
            return false;

        if(!canBuyTower(tower))
            return false;
        
        Tower currentTower;
        Enumeration<Tower> eTowers = towers.elements();
        while(eTowers.hasMoreElements())
        {
            currentTower = eTowers.nextElement();

            if (tower.intersects(currentTower))
                return false;
        }
        
        if(!tower.getOwner().getLocation().getZone().contains(
        		new Rect(tower.x(),tower.y(),tower.x()+tower.width(),tower.y()+tower.height())))
            return false;
        
        if(!game.getMap().canPlaceTower(tower))
            return false;

        Creature creature;
        Enumeration<Creature> eCreatures = game.getCreatures().elements();
        while(eCreatures.hasMoreElements())
        {
            creature = eCreatures.nextElement();
        
            if (tower.intersects(creature))
                return false;
        }
        
        return true;
    }

    // Can buy tower?
    public boolean canBuyTower(Tower tower)
    {
        if(tower != null)
            return (tower.getOwner().getGold() - tower.getPrice()) >= 0;
        
        return false;
    }

    // return tower
    public Tower getTower(int idTower)
    {
        Tower tower;
        Enumeration<Tower> eTowers = towers.elements();
        while(eTowers.hasMoreElements())
        {
            tower = eTowers.nextElement();
            
            if(tower.getId() == idTower)
                return tower;
        }
      
        return null;
    }

    public void destroy()
    {
        stopTowers();
        towers.clear();
    }
	
}
