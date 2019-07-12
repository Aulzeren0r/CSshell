public class StatsInterface implements Runnable {
    //Threaded interface which deals with creation/compilation of stat info for display. Currently under construction.
    private StatBuilder sb;
    private int stat_type;
    private int[] sub_type;
    private boolean release;

    public StatsInterface(DisplayWindow dw){
        //Init.
        stat_type = 0;
        sb = new StatBuilder(dw);
        sub_type = new int[3];
        release = false;
    }

    public void run() {
        //Under construction. to be destroyed and rebuilt.
        try {
            this.run_internal(this.stat_type, this.sub_type);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    //Public release switch. Will be toggle to make display visible on DisplayWindow.
    public void Release(){
        release = true;
    }
    private void run_internal(int type, int[] sub) throws InterruptedException {
        //Under construction.
        while (!release) {
            Thread.sleep(500);
        }

        switch (type) {
        }
    }
    //Outward-facing assignment functions.
    public void SetStatType(int i){
        stat_type = i;
    }

    public void SetSubTypeArray(int[] array){
        sub_type = array;
    }
}
