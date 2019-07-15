import org.json.JSONObject;

public class Champ {
    /* Custom variable class for champions and their specific data. Contains the following individual data for
     * each champion:
     * Kills
     * Deaths
     * Assists
     * Wins
     * Losses
     * Times Banned
     * CS/D at 10 Minutes (Not 15 due to Riot API restrictions)
     * Also stores a static list of champion IDs, used in champion declaration to determine their ID value.
     */
    private static JSONObject champ_ID_list;
    String name;
    private int wins;
    private int losses;
    private int tot_kills;
    private int tot_deaths;
    private int tot_assists;
    private int bans;
    String champ_id;
    private double CSDatTen;

    public static void PopulateChampIDs(){
        // Courtesy wrapper to pull champion IDs from DDragon prior to initial data loading.
        champ_ID_list = RiotAPICall.GetChampsList();
    }

    public static String GetID(String name){
        /* Searches the champ_ID_list JSONobject for the entry for the champion requested and returns the string
         * for that specific champion's key. Champion names are made safe, as explained below.
         */
        JSONObject champ = champ_ID_list.getJSONObject(name);
        return champ.getString("key");
    }

    public double GetKDA(){
        // Function for a champ which determines their average KDA.
        double ka = tot_kills + tot_assists;
        double d = tot_deaths;
        double kda_tot = ka/d;
        return kda_tot;
    }

    public double GetWinRate(){
        /* Function for a champion which determines their winrate as a decimal based upon the number of games in which
         * they have appeared.
         */
        double games = wins + losses;
        return ((double) wins)/games;
    }

    public void SetData(int w, int l, int k, int d, int a, double c){
        /* Secondary initialization function for a champ variable. Sets all datapoints to provided values.
         * SHOULD ONLY EVER BE CALLED FROM IO. For updates to champion stats, use InsertData().
         */
        SetWin(w);
        SetLoss(l);
        SetKill(k);
        SetDeath(d);
        SetAssist(a);
        SetCSD(c);
    }

    public void InsertData(boolean w, boolean l, boolean b, int k, int d, int a, double c){
        /* Function which updates champion stats based upon provided values. Should be used for any update to
         * champion statistics outside of initialization.
         */
        AddCSD(c);
        if(w){
            AddWin();
        }
        else if(l){
            AddLoss();
        }
        else if(b){
            AddBan();
        }
        AddKills(k);
        AddDeaths(d);
        AddAssists(a);
    }
    //The following functions are all self-explanatory wrappers.
    private void AddWin(){
        SetWin(wins ++);
    }

    private void AddLoss(){
        SetLoss(losses ++);
    }

    private void AddKills(int new_kills){
        SetKill(tot_kills + new_kills);
    }

    private void AddBan(){
        SetBan(bans ++);
    }

    private void AddDeaths(int new_deaths){
        SetDeath(tot_deaths + new_deaths);
    }

    private void AddAssists(int new_assists){
        SetAssist(tot_assists + new_assists);
    }

    private void AddCSD(double csd){
        double games = wins + losses;
        double tot_csd = (CSDatTen * games) + csd;
        double new_csd = tot_csd / (games + 1);
        SetCSD(new_csd);
    }

    public Champ(String new_name, String ID){
        //Initial variable declaration. Requires only a name and the champ's specific key.
        name = new_name;
        champ_id = ID;
    }

    public String[] Stringify(){
        /* Converts a champ variable to a series of strings for data storage. Format is as follows:
         * NAME
         * Wins-Losses
         * Kills-Deaths-Assists
         * CS/DatTen
         */
        String[] temp = new String[4];
        temp[0] = name;
        temp[1] = wins + "-" + losses;
        temp[2] = tot_kills + "-" + tot_deaths + "-" + tot_assists;
        temp[3] = Double.toString(CSDatTen);
        return temp;
    }

    //Further wrapper functions.
    private void SetWin(int w){
        wins = w;
    }

    private void SetLoss(int l){
        losses = l;
    }

    private void SetBan(int b){
        bans = b;
    }

    private void SetKill(int k){
        tot_kills = k;
    }

    private void SetDeath(int d){
        tot_deaths = d;
    }

    private void SetAssist(int a){
        tot_assists = a;
    }

    private void SetCSD(double c){
        CSDatTen = c;
    }
}
