public class Champ {
    String name;
    int[] wins;
    int[] losses;
    String loc;

    public Champ(String new_name, int[] new_wins, int[] new_losses, String loc){
        name = new_name;
        wins = new int[5];
        System.arraycopy(new_wins, 0, wins, 0, 5);
        losses = new int[5];
        System.arraycopy(new_losses, 0, losses, 0, 5);
    }

    public String[] Stringify(){
        String[] temp = new String[3];
        temp[0] = name;
        temp[1] = wins[0] + " " + wins[1] + " " + wins[2] + " " + wins[3] + " " + wins[4];
        temp[2] = losses[0] + " " + losses[1] + " " + losses[2] + " " + losses[3] + " " + losses[4];

        return temp;
    }
}
