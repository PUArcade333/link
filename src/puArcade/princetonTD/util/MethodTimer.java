package puArcade.princetonTD.util;

public class MethodTimer {

    public long startTime;
    public long endTime;
    
    public MethodTimer(){
            startTime=0;
            endTime=0;
    }
    
    public void start(){
            startTime = System.currentTimeMillis();
    }
    
    public void end(){
            endTime = System.currentTimeMillis();
    }
    
    public long getTimeInMilliseconds(){
            
            return endTime-startTime;
            
    }
    
    public double getTimeInSeconds(){
            return (endTime-startTime)/1000;
    }
    
    public void clearTime(){
            startTime = 0;
            endTime = 0;
    }
    
}

