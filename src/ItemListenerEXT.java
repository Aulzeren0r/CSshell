import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ItemListenerEXT implements ItemListener {
    /*Custom item listener. Used exclusively for role selection in NewTeamPageTwo. Role storage is handled using binary,
     * with each role being a value of 2^x, where x is between 0 and 4. This makes player data storage somewhat more
     * compact, and slightly harder to alter manually.
     */
    TopLevelRewrite window;
    public void itemStateChanged(ItemEvent e) {
        /* Determines which checkbox changed (i.e. what row and which role). Then, updates the role value of the given
         * player based on the role's assigned value. For both Active Role and Roles Available, the following are the
         * assignments:
         * Top: 1
         * Jungle: 2
         * Mid: 4
         * Bot: 8
         * Support: 16
         * This function also adjusts the dropdown menus for active role selection to include or disinclude the role
         * whose state was changed.
         */
        Object source = e.getItemSelectable();
        int checkbox_num = -1;
        int role_num = -1;
        int line_num = -1;
        int select_flag = -1;
        for(int i = 0; i < window.checkbox_array.length; i++){
            if(source.equals(window.checkbox_array[i])){
                checkbox_num = i;
                break;
            }
        }
        if(e.getStateChange() == ItemEvent.DESELECTED){
            select_flag = 0;
        }
        else if(e.getStateChange() == ItemEvent.SELECTED){
            select_flag = 1;
        }

        line_num = checkbox_num / 5;
        role_num = checkbox_num % 5;

        switch(role_num){
            case 0:
                if(select_flag == 1){
                    window.combo_array[line_num].addItem("Top");
                    if(window.loading_flag == 0) {
                        window.active_edit_team.roster[line_num].roles += 1;
                    }
                }
                else if(select_flag == 0){
                    window.combo_array[line_num].removeItem("Top");
                    window.active_edit_team.roster[line_num].roles -= 1;
                }
                break;
            case 1:
                if(select_flag == 1){
                    window.combo_array[line_num].addItem("Jungle");
                    if(window.loading_flag == 0) {
                        window.active_edit_team.roster[line_num].roles += 2;
                    }
                }
                else if(select_flag == 0){
                    window.combo_array[line_num].removeItem("Jungle");
                    window.active_edit_team.roster[line_num].roles -= 2;
                }
                break;
            case 2:
                if(select_flag == 1){
                    window.combo_array[line_num].addItem("Mid");
                    if(window.loading_flag == 0) {
                        window.active_edit_team.roster[line_num].roles += 4;
                    }
                }
                else if(select_flag == 0){
                    window.combo_array[line_num].removeItem("Mid");
                    window.active_edit_team.roster[line_num].roles -= 4;
                }
                break;
            case 3:
                if(select_flag == 1){
                    window.combo_array[line_num].addItem("Bot");
                    if(window.loading_flag == 0) {
                        window.active_edit_team.roster[line_num].roles += 8;
                    }
                }
                else if(select_flag == 0){
                    window.combo_array[line_num].removeItem("Bot");
                    window.active_edit_team.roster[line_num].roles -= 8;
                }
                break;
            case 4:
                if(select_flag == 1){
                    window.combo_array[line_num].addItem("Support");
                    if(window.loading_flag == 0) {
                        window.active_edit_team.roster[line_num].roles += 16;
                    }
                }
                else if(select_flag == 0){
                    window.combo_array[line_num].removeItem("Support");
                    window.active_edit_team.roster[line_num].roles -= 16;
                }
                break;
            default:
                break;
        }

    }
    public ItemListenerEXT(TopLevelRewrite focus_window){
        //Init. ItemListenerEXT is passed TLR.
        window = focus_window;
    }
}
