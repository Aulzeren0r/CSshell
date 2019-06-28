public class StatsInterface implements Runnable {
    StatBuilder sb;
    int stat_type;
    int[] sub_type;
    private boolean release;

    public StatsInterface(DisplayWindow dw){
        stat_type = 0;
        sb = new StatBuilder(dw);
        sub_type = new int[5];
        release = false;
    }

    public void run() {
        try {
            this.run_internal(this.stat_type, this.sub_type);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void Release(){
        release = true;
    }
    private void run_internal(int type, int[] sub) throws InterruptedException{
        while(!release){
            Thread.sleep(500);
        }

        switch(type){
            case SI.PLAYER:
                sb.SetFlag(0, 1);
                for (int value : sub) {
                    if (value == SI.CS) {
                        sb.SetFlag(1, 1);
                    } else if (value == SI.KDA) {
                        sb.SetFlag(1, 2);
                    } else if (value == SI.WINRATE) {
                        sb.SetFlag(1, 3);
                    } else if (value == SI.CSD) {
                        if (!sb.SetFlag(1, 4)) {
                            break;
                        }
                    } else if (value == SI.EXTENSION) {
                        if(!sb.SetFlag(1, 5)){
                            break;
                        }
                    } else if (value == SI.JUNG_PROX) {
                        if(!sb.SetFlag(1, 6)){
                            break;
                        }
                    }
                }
                break;
            case SI.CHAMP:
                break;
            case SI.LANE:
                break;
            case SI.MATCHUP:
                break;
            case SI.JUNGLE:
                break;
            default:
                break;
        }
    }

}
