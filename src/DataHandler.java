import org.json.JSONObject;

public class DataHandler {
    /* The major data storage class for the program. Contains a full list of current teams and champions, along with
     * all relevant data. Also stores data for the current game.
     */
    Team[] team_array;
    Champ[] champ_array;
    Team blue_team;
    Team red_team;
    String[] blue_champs;
    String[] blue_bans;
    String[] red_champs;
    String[] red_bans;
    TopLevelRewrite window;
    static int total_games;
    public static final int RED = 10;
    public static final int BLUE = 20;

    public DataHandler(TopLevelRewrite w){
        // Initialization of all variables to 0 or null for consistency.
        team_array = new Team[0];
        champ_array = new Champ[0];
        blue_team = null;
        red_team = null;
        blue_champs = new String[5];
        red_champs = new String[5];
        blue_bans = new String[5];
        red_bans = new String[5];
        window = w;
    }

    public void AddTeam(Team new_team){
        //Adds a team to team_array via the ExtendTeamArray helper function.
        for(int i = 0; i < team_array.length; i++){
            if(new_team.team_name.equals(team_array[i].team_name)){
                team_array[i] = new_team;
                return;
            }
        }
        int temp_int = team_array.length;
        team_array = ExtendTeamArray();
        team_array[temp_int] = new_team;
    }
    private Team[] ExtendTeamArray(){
        //Helper function for AddTeam that increases the size of team_array by one.
        Team[] temp = new Team[team_array.length + 1];
        System.arraycopy(team_array, 0, temp, 0, team_array.length);
        return temp;
    }

    public boolean RemoveTeam(String team_name){
        //Removes a given team from the team_array. Returns false on failure.
        for(int i = 0; i < team_array.length; i++){
            if(team_name.equals(team_array[i].team_name)){
                team_array = ConstrictTeamArray(i);

                return true;
            }
        }
        return false;
    }

    private Team[] ConstrictTeamArray(int location){
        //Decreases the size of team_array by one, shifting down all entries after a given location.
        for(int i = location + 1; i < team_array.length; i++){
            team_array[i - 1] = team_array[i];
        }
        Team[] temp_array = new Team[team_array.length - 1];
        System.arraycopy(team_array, 0, temp_array, 0, team_array.length - 1);

        return temp_array;
    }

    public void ListFill(int location, int flag){
        //Fills the lists of champions for the ChampSelectSwaps screen.
        switch (flag){
            case RED:
                for(int i = 0; i < 5; i++){
                    window.combo_array[location + 5].addItem(red_champs[i]);
                    window.combo_array[location + 5].setSelectedItem(red_champs[location]);
                }
                break;
            case BLUE:
                for(int i = 0; i < 5; i++){
                    window.combo_array[location].addItem(blue_champs[i]);
                    window.combo_array[location].setSelectedItem(blue_champs[location]);
                }
                break;
            default:
                break;
        }
    }

    public Champ FindChamp(String name){
        //Helper function to locate a Champ entry based on its name.
        for(int i = 0; i < champ_array.length; i++){
            if(champ_array[i].name.equals(name)){
                return champ_array[i];
            }
        }
        return null;
    }

    public Champ FindByKey(String key){
        for(int i = 0; i < champ_array.length; i++){
            if(champ_array[i].champ_id.equals(key)){
                return champ_array[i];
            }
        }
        return null;
    }

    public static Champ CheckChamps(Champ[] champ_array){
        Champ new_champ = null;
        for(String key : Champ.champ_ID_list.keySet()){
            int flag = 0;
            JSONObject champion = Champ.champ_ID_list.getJSONObject(key);
            String name = champion.getString("name");
            if(name.equals("Nunu & Willump") || name.equals("Kha'Zix")){
                flag = 1;
                continue;
            }
            for(Champ champ : champ_array){
                if(champ.name.equals(name)){
                    flag = 1;
                }
            }
            if(flag == 0){
                new_champ = new Champ(name, champion.getString("key"));
                new_champ.SetData(0,0,0, 0,0,0, 0.0);
            }
        }
        return new_champ;
    }
}
