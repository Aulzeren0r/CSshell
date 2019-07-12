/*
 * Copyright (c) Chris Attias, $month.year. Use of this code is reserved solely for the University of Alabama League of Legends Club.
 */

public class TimerThread implements Runnable {
    //Helper thread. Keeps time away from other program cycles in order to preserve a modicum of accuracy.
    private boolean exit;
    private boolean trigger;
    private int timer_length;
    private DisplayWindow target;
    private Thread t;

    public TimerThread(int time, DisplayWindow target_wind){
        //Init.
        exit = false;
        target = target_wind;
        timer_length = time;
        trigger = false;
        t = new Thread(this, "TimerThread");
        t.start();

    }
    @Override
    public void run() {
        while(!trigger){

        }
        int count = timer_length;
        while(!exit && count > 0){
            target.SetTimer(count);
            try{
                Thread.sleep(1000);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
            count--;
        }
        target.SetTimer(count);
    }

    //Public-facing stop and start functions.
    public void Stop(){
        exit = true;
    }
    public void Trigger() {
        trigger = true;
    }
}
