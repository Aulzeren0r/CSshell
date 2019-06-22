/*
 * Copyright (c) Chris Attias, $month.year. Use of this code is reserved solely for the University of Alabama League of Legends Club.
 */

public class TimerThread implements Runnable {
    private boolean exit;
    private int timer_length;
    DisplayWindow target;
    Thread t;
    public TimerThread(int time, DisplayWindow target_wind){
        exit = false;
        target = target_wind;
        timer_length = time;
        t = new Thread(this, "TimerThread");
        t.start();

    }
    @Override
    public void run() {
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

    public void Stop(){
        exit = true;
    }
}
