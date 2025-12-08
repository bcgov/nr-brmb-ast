package ca.bc.gov.srm.farm.util;

public class Stopwatch {
	private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;

    
    public Stopwatch(){}
    
    public Stopwatch(boolean start) {
    	super();
    	if(start) {
    		start();
    	}
    }
    
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    
    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    
    //elapsed time in milliseconds
    public long getElapsedTime() {
        long elapsed;
        if (running) {
             elapsed = (System.currentTimeMillis() - startTime);
        }
        else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }
    
    public long getElapsedTime(boolean stop) {
    	if (stop) {
    		stop();
    	}
    	return getElapsedTime();
    }
    
    
    //elapsed time in seconds
    public long getElapsedTimeSecs() {
        long elapsed;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000);
        }
        else {
            elapsed = ((stopTime - startTime) / 1000);
        }
        return elapsed;
    }
    
    public long getElapsedTimeSecs(boolean stop) {
    	if (stop) {
    		stop();
    	}
    	return getElapsedTimeSecs();
    }
}
