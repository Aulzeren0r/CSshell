import javax.swing.*;

public class Team {
    //Team variable class. Under construction pending Riot API upgrade.
    Player roster[];
    String team_name;
    String team_abbr;
    String logo_loc;
    private int wins;
    private int losses;
    private int kills;
    private int deaths;
    private int assists;
    private double gold;
    private double baron;
    private double inhib;
    private double tower;
    private double dragon;
    private int herald;
    private int first_drags;
    private double enemy_baron;
    private double enemy_drag;
    private int enemy_herald;

    //TODO Team Stats
    /* Wins
     * Losses
     * Team KDA
     * Team Gold
     * Baron Kills
     * Inhib Kills
     * Tower Kills
     * Dragon Kills
     * Herald Kills
     * First Dragon Rate
     * Enemy Baron Kills
     * Enemy Dragon Kills
     * Enemy Herald Kills
     *
     */

    //Public-facing assignment functions.
    public void AddWin(){
        wins = wins + 1;
    }

    public void AddLoss(){
        losses = losses + 1;
    }

    public void SetKill(int k){
        kills = k;
    }

    public void SetDeath(int d){
        deaths = d;
    }

    public void SetAssist(int a){
        assists = a;
    }

    public void SetGold(double g){
        gold = g;
    }

    public void SetBaron(double b){
        baron = b;
    }

    public void SetInhib(double i){
        inhib = i;
    }

    public void SetTower(double t){
        tower = t;
    }

    public void SetDrag(double d){
        dragon = d;
    }

    public void SetHerald(int h){
        herald = h;
    }

    public void SetFirstDrags(int f){
        first_drags = f;
    }

    public void SetEnemyBarons(double eb){
        enemy_baron = eb;
    }

    public void SetEnemyDrags(double ed){
        enemy_drag = ed;
    }

    public void SetEnemyHeralds(int eh){
        enemy_herald = eh;
    }

    public Team(Player[] pushed_roster, String new_name, String new_abbr, int win, int loss){
        //Init. Mostly straightforward.
        wins = win;
        losses = loss;
        team_abbr = new_abbr;
        team_name = new_name;
        roster = new Player[pushed_roster.length];
        System.arraycopy(pushed_roster, 0, roster, 0, pushed_roster.length);
        logo_loc = "/" + new_abbr + "/" + new_abbr + ".jpg";
    }

    public String[] Stringify(){
        /* Turns a Team variable into an array of strings for data storage in the configuration as follows:
         * Team name
         * Team abbr
         * win-loss
         * (Repeating)
         * (Player) First "Handle" Last
         * headshot_loc
         * roles active_role
         * (End Repeat)
         * ---
         */
        String[] return_array = new String[5 + (5 * this.roster.length)];
        return_array[0] = this.team_name;
        return_array[1] = this.team_abbr;
        return_array[2] = this.logo_loc;
        return_array[3] = this.wins + "-" + this.losses;

        for(int i = 0; i < this.roster.length; i++){
            return_array[4 + (5 * i)] = this.roster[i].first_name;
            return_array[5 + (5 * i)] = this.roster[i].handle;
            return_array[6 + (5 * i)] = this.roster[i].last_name;
            return_array[7 + (5 * i)] = this.roster[i].headshot_loc;
            return_array[8 + (5 * i)] = this.roster[i].roles + " " + this.roster[i].active_role;
            if(i == this.roster.length - 1){
                return_array[9 + (5 * i)] = "---";
            }
        }

        return return_array;
    }

    public static Team Destringify(String[] raw_data){
        //Takes an array of strings and turns it into a Team variable.
        Team temp_team = new Team(new Player[0], "", "", 0, 0);
        temp_team.team_name = raw_data[0];
        temp_team.team_abbr = raw_data[1];
        temp_team.logo_loc = raw_data[2];
        String[] temp_string_arr = raw_data[3].split("-");
        temp_team.wins = Integer.parseInt(temp_string_arr[0]);
        temp_team.losses = Integer.parseInt(temp_string_arr[1]);
        Player[] roster = new Player[7];
        int loc = 0;
        int i = 4;
        while(i < raw_data.length){
            String first_name = raw_data[i];
            i++;
            String handle = raw_data[i];
            i++;
            String last_name = raw_data[i];
            i++;
            String headshot_loc = raw_data[i];
            i++;
            String temp = raw_data[i];
            i++;
            String[] temp_array = temp.split(" ");

            int roles = Integer.parseInt(temp_array[0]);
            int active_role = Integer.parseInt(temp_array[1]);

            roster[loc] = new Player(first_name, handle, last_name, roles, active_role, "");
            roster[loc].headshot_loc = headshot_loc;
            loc++;
        }
        Player[] finished_roster = new Player[loc];
        System.arraycopy(roster, 0, finished_roster, 0, loc);
        temp_team.roster = finished_roster;
        return temp_team;
    }

    public String[] StringifyStats(){
        String[] temp = new String[5];
        temp[0] = kills + "-" + deaths + "-" + assists;
        temp[1] = gold + "~" + inhib + "~" + tower;
        temp[2] = baron + "~" + dragon + "~" + herald;
        temp[3] = Integer.toString(first_drags);
        temp[4] = enemy_baron + "~" + enemy_drag + "~" + enemy_herald;
        return temp;
    }

    public void DestringifyStats(String[] stats){
        String[] temp;
        temp = stats[0].split("-");
        kills = Integer.parseInt(temp[0]);
        deaths = Integer.parseInt(temp[1]);
        assists = Integer.parseInt(temp[2]);
        temp = stats[1].split("~");
        gold = Double.parseDouble(temp[0]);
        inhib = Double.parseDouble(temp[1]);
        tower = Double.parseDouble(temp[2]);
        temp = stats[2].split("~");
        baron = Double.parseDouble(temp[0]);
        dragon = Double.parseDouble(temp[1]);
        herald = Integer.parseInt(temp[2]);
        first_drags = Integer.parseInt(stats[3]);
        temp = stats[4].split("~");
        enemy_baron = Double.parseDouble(temp[0]);
        enemy_drag = Double.parseDouble(temp[1]);
        enemy_herald = Integer.parseInt(temp[2]);
    }

    public void StatUpdate(boolean w, int k, int d, int a, int g, int i, int t,
                           int b, int dr, int h, boolean fd, int eb, int ed, int eh){
        SetKill(kills + k);
        SetDeath(deaths + d);
        SetAssist(assists + a);
        double games = wins + losses;
        double temp = gold * games;
        temp += g;
        SetGold(temp / (games + 1));
        temp = inhib * games;
        temp += i;
        SetInhib(temp / (games + 1));
        temp = tower * games;
        temp += t;
        SetInhib(temp / (games + 1));
        temp = baron * games;
        temp += b;
        SetBaron(temp / (games + 1));
        temp = dragon * games;
        temp += dr;
        SetDrag(temp / (games + 1));
        SetHerald(herald + h);
        temp = enemy_baron * games;
        temp += eb;
        SetEnemyBarons(temp / (games + 1));
        temp = enemy_drag * games;
        temp += ed;
        SetEnemyDrags(temp / (games + 1));
        SetEnemyHeralds(enemy_herald + eh);
        if(fd){
            first_drags += 1;
        }
        if(w){
            AddWin();
        }
        else{
            AddLoss();
        }
    }

    //Data output functions.
    public int GetWins(){
        return wins;
    }

    public int GetLosses(){
        return losses;
    }

    public double GetKDA(){
        double ka = kills + assists;
        double kda = ka / (double) deaths;
        if(Double.isNaN(kda)){
            return -1;
        }
        return kda;
    }

    public double GetGold(){
        return gold;
    }

    public double GetBarons(){
        return baron;
    }

    public double GetInhibs(){
        return inhib;
    }

    public double GetTowers(){
        return tower;
    }

    public double GetDrags(){
        return dragon;
    }

    public int GetHerald(){
        return herald;
    }

    public double GetFDRate(){
        double games = wins + losses;
        if(Double.isNaN(first_drags/games)){
            return 0.0;
        }
        return first_drags / games;
    }

    public double GetEnemyBarons(){
        return enemy_baron;
    }

    public double GetEnemyDrags(){
        return enemy_drag;
    }

    public int GetEnemyHeralds(){
        return enemy_herald;
    }
}
