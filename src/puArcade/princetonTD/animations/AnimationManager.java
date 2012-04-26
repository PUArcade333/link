package puArcade.princetonTD.animations;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

import puArcade.princetonTD.game.Game;


public class AnimationManager implements Runnable {
	
	private static final long WAIT_TIME = 50;
    private Vector<Animation> animations = new Vector<Animation>();
    private Thread thread;
    private boolean inManagement;
    private boolean paused = false;
    private Object pause = new Object();
    private Game game;
    private long timeLeft = 0;
    
    // Constructor
    public AnimationManager(Game game)
    {
        this.game = game;
    }
    
    // Start
    public void start()
    {
        thread = new Thread(this);
        thread.start();
    }
    
    // add animation
    public void addAnimation(Animation animation)
    {
        animations.add(animation);
    }
    
    // draw animations
//    public void drawAnimations(Graphics2D g2, int height)
//    {
//        try
//        {
//            Enumeration<Animation> eAnimations = animations.elements();
//            Animation animation;
//            while(eAnimations.hasMoreElements())
//            {
//                animation = eAnimations.nextElement();
//                
//                if(animation.getHeight() == height)
//                    animation.draw(g2);
//            }
//        }
//        
//        catch(NoSuchElementException nse)
//        {
//            System.err.println("Cannot find animation");
//        }
//    }

    public void run()
    {
//       inManagement = true;
//
//       ArrayList<Animation> deleteAnimations = new ArrayList<Animation>();
//       Animation animation;
//       
//       while(inManagement)
//       {
//           timeLeft -= WAIT_TIME;
//           
//           if(timeLeft < 0)
//           { 
//
//               timeLeft = this.random(10000, 20000);
//           }
//
//           try
//           {
//               Enumeration<Animation> eAnimations = animations.elements();
//               while(eAnimations.hasMoreElements())
//               {
//                   animation = eAnimations.nextElement();
//
//                   if(animation.isFinished())
//                       deleteAnimations.add(animation);
//                   else
//                       animation.animate((long)(WAIT_TIME*game.getCoeffSpeed()));
//               }
//           }
//           catch(NoSuchElementException nse)
//           {
//               System.err.println("Cannot find animation");
//           }
// 
//           for(Animation deleteAnimation : deleteAnimations)
//               animations.remove(deleteAnimation);
//           deleteAnimations.clear();
//         
//           // pause
//           try
//           {
//               synchronized (pause)
//               {
//                   if(paused)
//                       pause.wait();
//               } 
//           } 
//           catch (InterruptedException e1)
//           {
//               e1.printStackTrace();
//           }
//           
//           try{
//                Thread.sleep(WAIT_TIME);
//           } 
//           catch (InterruptedException e){
//                e.printStackTrace();
//           }
//       }
    }
    
    private long random(int min, int max) {
    	return min + (int) Math.round(Math.random() * (max - min)); 
	}

	// Stop management
    public void stopAnimations()
    {
        inManagement = false;
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

    public void destroy()
    {
        stopAnimations();
        animations.clear();
    }
	
}
