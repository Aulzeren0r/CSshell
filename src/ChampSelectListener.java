import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class ChampSelectListener implements ActionListener {
    /* Special ActionListener exclusively for ChampSelectPickBan() and ChampSelectSwaps().
     * Built separately from ExtendedListener because the code required for champ select functionality
     * is bulky and long, which would make ExtendedListener harder than it already is to understand on read.
     */
    TopLevelRewrite window;
    private static final int BLUE = 1;
    private static final int RED = 2;
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("champ_select_continue")) {
            // Action command for submitting a pick or ban. Stores the data and advances to the next selection.
            String curr_sel = null;
            if (EditCheck()) {
                curr_sel = (String) window.auto_array[0].getSelectedItem();
            } else {
                curr_sel = (String) window.auto_array[1].getSelectedItem();
            }

            window.s.RemoveItem(curr_sel);
            window.auto_array[0].NewSearchable(window.s);
            window.auto_array[1].NewSearchable(window.s);
            String temp;
            switch (window.champ_sel_flag) {
                case 0:
                    window.data.blue_bans[0] = curr_sel;
                    window.label_array[24].setText(curr_sel);
                    window.label_array[1].setText("Red 1");
                    EditFlip();
                    break;
                case 1:
                    window.data.red_bans[0] = curr_sel;
                    window.label_array[29].setText(curr_sel);
                    window.label_array[1].setText("Blue 2");
                    EditFlip();
                    break;
                case 2:
                    window.data.blue_bans[1] = curr_sel;
                    window.label_array[25].setText(curr_sel);
                    window.label_array[1].setText("Red 2");
                    EditFlip();
                    break;
                case 3:
                    window.data.red_bans[1] = curr_sel;
                    window.label_array[30].setText(curr_sel);
                    window.label_array[1].setText("Blue 3");
                    EditFlip();
                    break;
                case 4:
                    window.data.blue_bans[2] = curr_sel;
                    window.label_array[26].setText(curr_sel);
                    window.label_array[1].setText("Red 3");
                    EditFlip();
                    break;
                case 5:
                    window.data.red_bans[2] = curr_sel;
                    window.label_array[31].setText(curr_sel);
                    window.label_array[0].setText("Current Pick:");
                    window.label_array[1].setText("Blue 1");
                    EditFlip();
                    break;
                case 6:
                    window.data.blue_champs[0] = curr_sel;
                    window.label_array[14].setText(curr_sel);
                    window.label_array[1].setText("Red 1");
                    EditFlip();
                    break;
                case 7:
                    window.data.red_champs[0] = curr_sel;
                    window.label_array[19].setText(curr_sel);
                    window.label_array[1].setText("Red 2");
                    break;
                case 8:
                    window.data.red_champs[1] = curr_sel;
                    window.label_array[20].setText(curr_sel);
                    window.label_array[1].setText("Blue 2");
                    EditFlip();
                    break;
                case 9:
                    window.data.blue_champs[1] = curr_sel;
                    window.label_array[15].setText(curr_sel);
                    window.label_array[1].setText("Blue 3");
                    break;
                case 10:
                    window.data.blue_champs[2] = curr_sel;
                    window.label_array[16].setText(curr_sel);
                    window.label_array[1].setText("Red 3");
                    EditFlip();
                    break;
                case 11:
                    window.data.red_champs[2] = curr_sel;
                    window.label_array[21].setText(curr_sel);
                    window.label_array[0].setText("Current Ban:");
                    window.label_array[1].setText("Red 4");
                    break;
                case 12:
                    window.data.red_bans[3] = curr_sel;
                    window.label_array[32].setText(curr_sel);
                    window.label_array[1].setText("Blue 4");
                    EditFlip();
                    break;
                case 13:
                    window.data.blue_bans[3] = curr_sel;
                    window.label_array[27].setText(curr_sel);
                    window.label_array[1].setText("Red 5");
                    EditFlip();
                    break;
                case 14:
                    window.data.red_bans[4] = curr_sel;
                    window.label_array[33].setText(curr_sel);
                    window.label_array[1].setText("Blue 5");
                    EditFlip();
                    break;
                case 15:
                    window.data.blue_bans[4] = curr_sel;
                    window.label_array[28].setText(curr_sel);
                    window.label_array[0].setText("Current Pick");
                    window.label_array[1].setText("Red 4");
                    EditFlip();
                    break;
                case 16:
                    window.data.red_champs[3] = curr_sel;
                    window.label_array[22].setText(curr_sel);
                    window.label_array[1].setText("Blue 4");
                    EditFlip();
                    break;
                case 17:
                    window.data.blue_champs[3] = curr_sel;
                    window.label_array[17].setText(curr_sel);
                    window.label_array[1].setText("Blue 5");
                    break;
                case 18:
                    window.data.blue_champs[4] = curr_sel;
                    window.label_array[18].setText(curr_sel);
                    window.label_array[1].setText("Red 5");
                    EditFlip();
                    break;
                case 19:
                    window.data.red_champs[4] = curr_sel;
                    window.label_array[23].setText(curr_sel);
                    window.ChampSelectSwaps();
                default:
                    break;
            }

            window.PublicRefresh();
            window.champ_sel_flag++;
        }
        else if(e.getActionCommand().equals("champ_select_swap")){
            //Action command for swap submission. Updates champion placements as needed.
            int[] blue_count = new int[5];
            int[] red_count = new int[5];
            for(int i = 0; i < 5; i++){
                blue_count[i] = 0;
                red_count[i] = 0;
            }
            for(int i = 0; i < 5; i++){
                String blue_sel = (String) window.combo_array[i].getSelectedItem();
                String red_sel = (String) window.combo_array[5 + i].getSelectedItem();
                for(int j = 0; j < 5; j++){
                    if(blue_sel.equals(window.data.blue_champs[j])){
                        blue_count[j] ++;
                    }
                    if(red_sel.equals(window.data.red_champs[j])){
                        red_count[j] ++;
                    }
                }
            }
            for(int i = 0; i < 5; i++){
                if(!(blue_count[i] == 1) || !(red_count[i] == 1)){
                    JOptionPane.showMessageDialog(window.main_frame, "Each champion must occupy only one position.",
                            "Champion Swap Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            for(int i = 0; i < 5; i++){
                blue_count[i] = -1;
                red_count[i] = -1;
            }
            for(int i = 0; i < 5; i++){
                String blue_sel = (String) window.combo_array[i].getSelectedItem();
                String red_sel = (String) window.combo_array[i + 5].getSelectedItem();
                for(int j = 0; j < 5; j++){
                    if(blue_sel.equals(window.data.blue_champs[j])){
                        blue_count[i] = j;
                    }
                    if(red_sel.equals(window.data.red_champs[j])){
                        red_count[i] = j;
                    }
                }
            }
            String[] blue_copy = new String[5];
            String[] red_copy = new String[5];
            System.arraycopy(window.data.blue_champs, 0, blue_copy, 0, 5);
            System.arraycopy(window.data.red_champs, 0, red_copy, 0, 5);
            for(int i = 0; i < 5; i++){
                window.data.blue_champs[i] = blue_copy[blue_count[i]];
                window.data.red_champs[i] = red_copy[red_count[i]];


                window.label_array[14 + i].setText(window.data.blue_champs[i]);
                window.label_array[19 + i].setText(window.data.red_champs[i]);
            }

        }


    }

    public ChampSelectListener(TopLevelRewrite w){
        window = w;
    }

    private void EditFlip() {
        /* Changes which AutoComplete box is currently editable.
         * Timeout implemented to allow AutocompleteJComboBox to properly update because its implementation breaks
         * if the setEditable at the end is altered.
         */
        try {
            if (window.auto_array[0].isEditable()) {
                TimeUnit.MILLISECONDS.sleep(100);
                window.auto_array[0].setEditable(false);
                window.auto_array[1].setEditable(true);
                window.auto_array[0].setSelectedItem("");
            } else {
                TimeUnit.MILLISECONDS.sleep(100);
                window.auto_array[0].setEditable(true);
                window.auto_array[1].setEditable(false);
                window.auto_array[1].setSelectedItem("");
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private boolean EditCheck(){
        //Checks if blue side is currently editable.
        if(window.auto_array[0].isEditable()){
            return true;
        }
        else{
            return false;
        }
    }
}
