import javax.swing.*;
import java.awt.event.WindowEvent;
import java.lang.Math;

public class PullData {
    TopLevelRewrite window;
    public Team PullPageOne(){
        Team temp_team;
        String team_name = window.text_field_array[0].getText();
        String team_abbr = null;
        if(window.text_field_array[1].getText().length() < 4) {
            team_abbr = window.text_field_array[1].getText();
        }
        else{
            window.handler.error_flag = 2;
            return null;
        }
        Player[] temp_roster = CreateNewRoster(team_abbr);
        if(temp_roster == null){
            return null;
        }
        temp_team = new Team(temp_roster, team_name, team_abbr, 0, 0);
        return temp_team;

    }

    public Player[] CreateNewRoster(String team_abbr){
        String[] first_names;
        String[] handles;
        String[] last_names;
        Player[] temp_roster;
        int i = 0;
        while(i < 7 && !(window.text_field_array[2 + (3 * i)].getText().equals(""))){
            i++;
        }
        first_names = PullFirsts(i);
        handles = PullHandles(i);
        last_names = PullLasts(i);
        temp_roster = new Player[i];

        if(window.handler.error_flag == 1){
            return null;
        }

        if(window.handler.update_flag == 1) {
            for (int j = 0; j < i; j++) {
                temp_roster[j] = new Player(first_names[j], handles[j], last_names[j], window.active_edit_team.roster[j].roles,
                        window.active_edit_team.roster[j].active_role, team_abbr);
            }

        }
        else{
            for(int j = 0; j < i; j++){
                temp_roster[j] = new Player(first_names[j], handles[j], last_names[j], 0, 0, team_abbr);
            }
        }
        return temp_roster;
    }

    public void PushPageOneData(){
        window.text_field_array[0].setText(window.active_edit_team.team_name);
        window.text_field_array[1].setText(window.active_edit_team.team_abbr);
        for(int i = 0; i < window.active_edit_team.roster.length; i++){
            window.text_field_array[2 + (3 * i)].setText(window.active_edit_team.roster[i].first_name);
            window.text_field_array[3 + (3 * i)].setText(window.active_edit_team.roster[i].handle);
            window.text_field_array[4 + (3 * i)].setText(window.active_edit_team.roster[i].last_name);
        }
    }

    public void PushPageTwoData(){
        window.loading_flag = 1;
        for(int i = 0; i < window.active_edit_team.roster.length && i < 7; i++){
            int roles = window.active_edit_team.roster[i].roles;
            for(int j = 0; j < 5; j++){
                int tag = roles % 2;
                if(tag == 1){
                    window.checkbox_array[(5 * i) + j].setSelected(true);
                }
                roles = roles / 2;
                if(roles == 0){
                    break;
                }
            }
            int active = window.active_edit_team.roster[i].active_role;
            switch(active){
                case 0:
                    window.combo_array[i].setSelectedItem("Bench");
                    break;
                case 1:
                    window.combo_array[i].setSelectedItem("Top");
                    break;
                case 2:
                    window.combo_array[i].setSelectedItem("Jungle");
                    break;
                case 4:
                    window.combo_array[i].setSelectedItem("Mid");
                    break;
                case 8:
                    window.combo_array[i].setSelectedItem("Bot");
                    break;
                case 16:
                    window.combo_array[i].setSelectedItem("Support");
                    break;
                default:
                    break;
            }

        }
        window.loading_flag = 0;
    }
    public void PullTeamNameAbbr(){
        window.active_edit_team.team_name = window.text_field_array[0].getText();
        if(window.text_field_array[1].getText().length() < 4){

        }
    }

    public PullData(TopLevelRewrite passed_window){
        window = passed_window;
    }

    private String[] PullFirsts(int count){
        String[] first_names = null;
        if(count == 7){
            first_names = new String[7];
        }
        else if(count == 6){
            first_names = new String[6];
        }
        else if(count == 5){
            first_names = new String[5];
        }
        else if(count < 5){
            window.handler.error_flag = 1;
            return null;
        }
        switch(count){
            case 7:
                first_names[6] = window.text_field_array[20].getText();
            case 6:
                first_names[5] = window.text_field_array[17].getText();
            case 5:
                first_names[4] = window.text_field_array[14].getText();
                first_names[3] = window.text_field_array[11].getText();
                first_names[2] = window.text_field_array[8].getText();
                first_names[1] = window.text_field_array[5].getText();
                first_names[0] = window.text_field_array[2].getText();
            default:
                break;
        }
        return first_names;
    }
    private String[] PullHandles(int count){
        String[] handles = null;
        if(count == 7){
            handles = new String[7];
        }
        else if(count == 6){
            handles = new String[6];
        }
        else if(count == 5){
            handles = new String[5];
        }
        else if(count < 5){
            return null;
        }
        switch(count){
            case 7:
                handles[6] = window.text_field_array[21].getText();
            case 6:
                handles[5] = window.text_field_array[18].getText();
            case 5:
                handles[4] = window.text_field_array[15].getText();

                handles[3] = window.text_field_array[12].getText();

                handles[2] = window.text_field_array[9].getText();
                handles[1] = window.text_field_array[6].getText();
                handles[0] = window.text_field_array[3].getText();
            default:
                break;
        }
        return handles;
    }
    private String[] PullLasts(int count){
        String[] last_names = null;
        if(count == 7){
            last_names = new String[7];
        }
        else if(count == 6){
            last_names = new String[6];
        }
        else if(count == 5){
            last_names = new String[5];
        }
        else if(count < 5){
            return null;
        }
        switch(count){
            case 7:
                last_names[6] = window.text_field_array[22].getText();
            case 6:
                last_names[5] = window.text_field_array[19].getText();
            case 5:
                last_names[4] = window.text_field_array[16].getText();
                last_names[3] = window.text_field_array[13].getText();
                last_names[2] = window.text_field_array[10].getText();
                last_names[1] = window.text_field_array[7].getText();
                last_names[0] = window.text_field_array[4].getText();
            default:
                break;
        }
        return last_names;
    }

    public int PullActiveRoles(){
        int top_flag = 0;
        int jung_flag = 0;
        int mid_flag = 0;
        int bot_flag = 0;
        int supp_flag = 0;
        for(int i = 0; i < window.active_edit_team.roster.length; i++){
            Object curr_sel = window.combo_array[i].getSelectedItem();
            if(curr_sel.equals("Top")){
                window.active_edit_team.roster[i].active_role = 1;
                top_flag ++;
            }
            else if(curr_sel.equals("Jungle")){
                window.active_edit_team.roster[i].active_role = 2;
                jung_flag ++;
            }
            else if(curr_sel.equals("Mid")){
                window.active_edit_team.roster[i].active_role = 4;
                mid_flag ++;
            }
            else if(curr_sel.equals("Bot")){
                window.active_edit_team.roster[i].active_role = 8;
                bot_flag ++;
            }
            else if(curr_sel.equals("Support")){
                window.active_edit_team.roster[i].active_role = 16;
                supp_flag ++;
            }
            else if(curr_sel.equals("Bench")){
                window.active_edit_team.roster[i].active_role = 0;
            }
        }
        if(top_flag == 1 && jung_flag == 1 && mid_flag == 1 && bot_flag == 1 && supp_flag == 1){
            return 1;
        }
        else if(top_flag > 1 || jung_flag > 1 || mid_flag > 1 || bot_flag > 1 || supp_flag > 1){
            return 2;
        }
        else if(top_flag == 0 || jung_flag == 0 || mid_flag == 0 || bot_flag == 0 || supp_flag == 0){
            return 3;
        }
        else{
            return -5;
        }

    }
}
