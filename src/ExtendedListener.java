import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ExtendedListener implements ActionListener {
    /* The main ActionHandler for TLR. Each ActionCommand branch will notate source and output locations, if possible.
     * PullData built specifically for ExtendedListener to retrieve data from JFrame components.
     */
    TopLevelRewrite main_window;
    PullData data_handler;
    int update_flag;
    int error_flag;
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("new_team")){
            //Origin: TopLevelRewrite, Ln 148
            //Output: TopLevelRewrite, Ln 220
            main_window.NewTeam();
            update_flag = 0;
        }
        else if(e.getActionCommand().equals("update_team")){
            //Origin: TopLevelRewrite, Ln 155
            //Output: TopLevelRewrite, Ln 299
            main_window.page_flag = 0;
            main_window.UpdateTeam();
            update_flag = 1;
        }
        else if(e.getActionCommand().equals("delete_team")){
            //Origin: TopLevelRewrite, Ln 162
            //Output: TopLevelRewrite, Ln 358
            main_window.page_flag = 0;
            main_window.DeleteTeam();
        }
        else if(e.getActionCommand().equals("new_team_page_1")){
            //Origin: TopLevelRewrite, Ln 232
            if(update_flag == 0) {
                //Output: PullData, Ln 7 -> active_edit_team
                main_window.active_edit_team = data_handler.PullPageOne();
            }
            else if(update_flag == 1){
                //Output: PullData, ln 111 (Deprecated)
                data_handler.PullTeamNameAbbr();
                main_window.active_edit_team.roster = data_handler.CreateNewRoster(main_window.active_edit_team.team_abbr);
            }
            if(error_flag == 1){
                //Output inline
                JOptionPane.showMessageDialog(main_window.main_frame, "A valid team requires at least 5 players.\n" +
                        "Ensure that each player has a first name, handle and last name.",
                        "Roster Creation Error", JOptionPane.ERROR_MESSAGE);
                error_flag = 0;
                return;
            }
            else if(error_flag == 2){
                //Output inline
                JOptionPane.showMessageDialog(main_window.main_frame, "Team Abbreviation must be 3 characters or less.",
                        "Roster Creation Error", JOptionPane.ERROR_MESSAGE);
                error_flag = 0;
                return;
            }
            //Output: TopLevelRewrite, Ln 427
            main_window.NewTeamPageTwo();
            if(update_flag == 1){
                //Output: PullData, Ln 70
                data_handler.PushPageTwoData();
            }
        }
        else if(e.getActionCommand().equals("new_team_two_back")){
            //Origin: TopLevelRewrite, Ln 488
            //Output: TopLevelRewrite, Ln 220
            //        PullData, Ln 60
            main_window.NewTeam();
            data_handler.PushPageOneData();
        }
        else if(e.getActionCommand().equals("finish_new_team")){
            //Origin: TopLevelRewrite, Ln 496
            //Output: PullData, Ln 216 -> temp
            int temp = data_handler.PullActiveRoles();
            if(temp == 1) {
                //Output: DataHandler, Ln 31
                //        TopLevelRewrite, Ln 110
                main_window.data.AddTeam(main_window.active_edit_team);
                main_window.PopulateLander();
            }
            else if(temp == 2){
                //Output inline
                JOptionPane.showMessageDialog(main_window.main_frame, "You may not select more than one active player per role.",
                        "Role Selection Error", JOptionPane.ERROR_MESSAGE);
            }
            else if(temp == 3){
                //Output inline
                JOptionPane.showMessageDialog(main_window.main_frame, "You must select a player for each role.",
                        "Role Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(e.getActionCommand().equals("update_back_page")){
            //Origin: TopLevelRewrite, Ln 338
            //Output: TopLevelRewrite, Ln 299
            main_window.page_flag --;
            main_window.UpdateTeam();
        }
        else if(e.getActionCommand().equals("update_next_page")){
            //Origin: TopLevelRewrite, Ln 348
            //Output: TopLevelRewrite, Ln 299
            main_window.page_flag ++;
            main_window.UpdateTeam();
        }
        else if(e.getActionCommand().equals("push_team_update")){
            //Origin: TopLevelRewrite, Ln 327 (10 Objects)
            Object command_dist = e.getSource();

            int button_no = 0;
            while(!(main_window.button_array[button_no].equals(command_dist))){
                button_no ++;
            }
            main_window.active_edit_team = main_window.data.team_array[button_no + (10 * main_window.page_flag)];
            //Output: TopLevelRewrite, Ln 220
            //        PullData, Ln 60
            main_window.NewTeam();
            data_handler.PushPageOneData();

        }
        else if(e.getActionCommand().equals("push_team_delete")){
            //Origin: TopLevelRewrite, Ln 374 (10 Objects)
            Object command_dist = e.getSource();

            int button_no = 0;
            while(!(main_window.button_array[button_no].equals(command_dist))){
                button_no ++;
            }
            String confirm_string = "Are you sure you wish to permanently remove " +
                    main_window.data.team_array[(main_window.page_flag * 10) + button_no].team_name +
                    "? This cannot be undone.";

            int n = JOptionPane.showConfirmDialog(main_window.main_frame, confirm_string, "Are You Sure?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(n == JOptionPane.YES_OPTION){
                //Output: DataHandler, Ln 50
                //        TopLevelRewrite, Ln 358
                main_window.data.RemoveTeam(main_window.data.team_array[(main_window.page_flag * 10) + button_no].team_name);
                main_window.page_flag = 0;
                main_window.DeleteTeam();
            }
            else if(n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION){
                return;
            }
        }
        else if(e.getActionCommand().equals("champ_select_start")){
            //Origin: TopLevelRewrite, Ln 173
            if(main_window.data.team_array.length > 1) {
                //Output: TopLevelRewrite, Ln 563
                main_window.ChampSelect();
            }
            else{
                //Output inline
                JOptionPane.showMessageDialog(main_window.main_frame, "You must have at least two teams to begin champ select.",
                        "Champ Select Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(e.getActionCommand().equals("champ_select_teams")){
            //Origin: TopLevelRewrite, Ln 584
            if(main_window.combo_array[0].getSelectedItem().equals(main_window.combo_array[1].getSelectedItem())){
                //Output inline
                JOptionPane.showMessageDialog(main_window.main_frame, "You must select two different teams.",
                        "Champ Select Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                Team temp_blue = null;
                Team temp_red = null;
                for(int i = 0; i < main_window.data.team_array.length; i++){
                    if(main_window.combo_array[0].getSelectedItem().equals(main_window.data.team_array[i].team_name)){
                        temp_blue = main_window.data.team_array[i];
                    }
                    if(main_window.combo_array[1].getSelectedItem().equals(main_window.data.team_array[i].team_name)){
                        temp_red = main_window.data.team_array[i];
                    }
                }
                main_window.data.blue_team = temp_blue;
                main_window.data.red_team = temp_red;
                //Output: TopLevelRewrite, Ln 612
                main_window.ChampSelectPickBan();
            }

        }
        else if(e.getActionCommand().equals("force_display_quit")){
            //Origin: TopLevelRewrite, Ln 186
            //Output inline (Closes DisplayWindow)
            main_window.screen.main_frame.dispatchEvent(new WindowEvent(main_window.screen.main_frame, WindowEvent.WINDOW_CLOSING));
        }
        else if(e.getActionCommand().equals("force_display_hide")){
            //Origin: TopLevelRewrite, Ln 180
            //Output: DisplayWindow, Ln 47
            main_window.screen.SetVis();
        }

    }

    public ExtendedListener(TopLevelRewrite window){
        //Init. Creates new PullData, defaults flags to 0.
        main_window = window;
        data_handler = new PullData(main_window);
        update_flag = 0;
        error_flag = 0;
    }
}
