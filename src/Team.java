public class Team {
    Player roster[];
    String team_name;
    String team_abbr;
    String logo_loc;
    int wins;
    int losses;

    public void AddWin(){
        wins = wins + 1;
    }

    public void AddLoss(){
        losses = losses + 1;
    }

    public Team(Player[] pushed_roster, String new_name, String new_abbr, int win, int loss){
        wins = win;
        losses = loss;
        team_abbr = new_abbr;
        team_name = new_name;
        roster = new Player[pushed_roster.length];
        System.arraycopy(pushed_roster, 0, roster, 0, pushed_roster.length);
        logo_loc = "/" + new_abbr + "/" + new_abbr + ".jpg";
    }

    public String[] Stringify(){
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
}
