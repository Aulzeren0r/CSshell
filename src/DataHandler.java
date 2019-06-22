public class DataHandler {
    Team[] team_array;
    Champ[] champ_array;
    Team blue_team;
    Team red_team;
    String[] blue_champs;
    String[] blue_bans;
    String[] red_champs;
    String[] red_bans;
    TopLevelRewrite window;
    public static final int RED = 10;
    public static final int BLUE = 20;

    public DataHandler(TopLevelRewrite w){
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
        Team[] temp = new Team[team_array.length + 1];
        System.arraycopy(team_array, 0, temp, 0, team_array.length);
        return temp;
    }

    public boolean RemoveTeam(String team_name){
        for(int i = 0; i < team_array.length; i++){
            if(team_name.equals(team_array[i].team_name)){
                team_array = ConstrictTeamArray(i);

                return true;
            }
        }
        return false;
    }

    private Team[] ConstrictTeamArray(int location){
        for(int i = location + 1; i < team_array.length; i++){
            team_array[i - 1] = team_array[i];
        }
        Team[] temp_array = new Team[team_array.length - 1];
        System.arraycopy(team_array, 0, temp_array, 0, team_array.length - 1);

        return temp_array;
    }

    public void ListFill(int location, int flag){
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
        for(int i = 0; i < champ_array.length; i++){
            if(champ_array[i].name.equals(name)){
                return champ_array[i];
            }
        }
        return null;
    }
}
