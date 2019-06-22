import java.util.List;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class TopLevelRewrite {
    JFrame main_frame;
    Container main_panel;
    JTextField[] text_field_array;
    JButton[] button_array;
    JLabel[] label_array;
    JCheckBox[] checkbox_array;
    JComboBox[] combo_array;
    AutocompleteJComboBox[] auto_array;
    ExtendedListener handler;
    Team active_edit_team;
    ItemListenerEXT item_handler;
    ChampSelectListener champ_handler;
    IO io;
    DataHandler data;
    StringSearchable s;
    DisplayWindow screen;
    int page_flag;
    int loading_flag;
    int champ_sel_flag;
    
    public static void main(String[] args) throws IOException {
        TopLevelRewrite new_window = new TopLevelRewrite();
        new_window.io = new IO(new_window);
        new_window.data = new DataHandler(new_window);
        new_window.RunStartUp();
        new_window.handler = new ExtendedListener(new_window);
        new_window.item_handler = new ItemListenerEXT(new_window);
        new_window.champ_handler = new ChampSelectListener(new_window);
        new_window.loading_flag = 0;
        new_window.champ_sel_flag = 0;
        new_window.screen = new DisplayWindow(new_window, new_window.data);
        new_window.InitMenus();
        new_window.PopulateLander();
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new_window.StartWindow();
            }
        });
        new_window.screen.SetVis();
        new_window.screen.ForceFront();

    }

    public TopLevelRewrite(){
        main_frame = new JFrame();
        main_frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        WindowListener InternalListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DataWrite();
                io.CloseThreads();
                main_frame.dispose();
                System.exit(0);
            }
        };
        main_frame.addWindowListener(InternalListener);
        main_panel = main_frame.getContentPane();
        main_panel.setLayout(new GridBagLayout());
        page_flag = 0;
    }



    public void PopulateLander(){
        ClearWindow();
        main_panel.repaint();
        JLabel main_label = new JLabel("Welcome to the Champ Select Overlay / Display Application!");
        JLabel sub_label = new JLabel("To get started, make a selection from the menus above.");
        JLabel creator = new JLabel("Written by Chris Attias, 2019");
        JLabel blank_label = new JLabel("");
        JLabel blank_label_2 = new JLabel("");
        GridBagConstraints constraints = CNC(0, 0, 3, 1, GridBagConstraints.NONE,
                300, 30, GridBagConstraints.CENTER, new Insets(0,0,0,0));
        main_panel.add(blank_label, constraints);
        constraints = CNC(1, 1, 1, 1, GridBagConstraints.NONE, 0, 10,
                GridBagConstraints.CENTER, new Insets(5, 10, 5, 10));
        main_panel.add(main_label, constraints);
        constraints = CNC(1, 2, 1, 1, GridBagConstraints.NONE, 0, 10,
                GridBagConstraints.CENTER, new Insets(5, 10, 5, 10));
        main_panel.add(sub_label, constraints);
        constraints = CNC(0, 3, 3, 1, GridBagConstraints.NONE, 100, 30,
                GridBagConstraints.CENTER, new Insets(0,0,0,0));
        main_panel.add(blank_label_2, constraints);
        constraints = CNC(2, 4, 1, 1, GridBagConstraints.NONE, 0, 10,
                GridBagConstraints.CENTER, new Insets(5, 10, 5, 10));
        main_panel.add(creator, constraints);
        RefreshWindow();
    }

    private void InitMenus(){
        JMenuBar build_bar = new JMenuBar();
        JMenu temp_menu = new JMenu("Teams");

        JMenuItem temp_item = new JMenuItem("Create Team");
        temp_item.setActionCommand("new_team");
        temp_item.addActionListener(handler);
        temp_item.setMnemonic(KeyEvent.VK_C);
        temp_item.getAccessibleContext().setAccessibleDescription("Create and store a new team for future use.");
        temp_menu.add(temp_item);

        temp_item = new JMenuItem("Update Team");
        temp_item.setActionCommand("update_team");
        temp_item.addActionListener(handler);
        temp_item.setMnemonic(KeyEvent.VK_U);
        temp_item.getAccessibleContext().setAccessibleDescription("Update an already-created team to reflect changes.");
        temp_menu.add(temp_item);

        temp_item = new JMenuItem("Remove Team(s)");
        temp_item.setActionCommand("delete_team");
        temp_item.addActionListener(handler);
        temp_item.setMnemonic(KeyEvent.VK_R);
        temp_item.getAccessibleContext().setAccessibleDescription("Remove teams from your list of saved teams.");
        temp_menu.add(temp_item);

        build_bar.add(temp_menu);

        temp_menu = new JMenu("Display");

        temp_item = new JMenuItem("Start Champ Select");
        temp_item.setActionCommand("champ_select_start");
        temp_item.addActionListener(handler);
        temp_item.setMnemonic(KeyEvent.VK_S);
        temp_item.getAccessibleContext().setAccessibleDescription("Starts the champ select function.");
        temp_menu.add(temp_item);

        temp_item = new JMenuItem("Toggle Display");
        temp_item.setActionCommand("force_display_hide");
        temp_item.addActionListener(handler);
        temp_item.setMnemonic(KeyEvent.VK_T);
        temp_menu.add(temp_item);

        temp_item = new JMenuItem("Force-Close Display");
        temp_item.setActionCommand("force_display_quit");
        temp_item.addActionListener(handler);
        temp_menu.add(temp_item);

        build_bar.add(temp_menu);
        main_frame.setJMenuBar(build_bar);
    }

    private GridBagConstraints CNC(int xloc, int yloc, int xsize, int ysize, int fill, int intpadx,
                                                           int intpady, int anchor, Insets inset){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = xloc;
        c.gridy = yloc;
        c.gridwidth = xsize;
        c.gridheight = ysize;
        c.fill = fill;
        c.ipadx = intpadx;
        c.ipady = intpady;
        c.anchor = anchor;
        c.insets = inset;
        return c;
    }

    private void StartWindow(){
        main_frame.setVisible(true);
    }

    public void NewTeam(){
        //TODO Finish NewTeam Window
        /* Layout
         * Finish ExtendedListener handler
         * Build Window 2 (role selection)
         */
        ClearWindow();
        JLabel blank_label = new JLabel("");
        JLabel blank_label_2 = new JLabel("");
        GridBagConstraints constraints;
        Insets default_inset = new Insets(5, 10, 5, 10);
        String label_1_string = "Enter Roster Information for ";
        button_array = new JButton[1];
        button_array[0] = new JButton("Next Page");
        button_array[0].setActionCommand("new_team_page_1");
        button_array[0].addActionListener(handler);
        button_array[0].getAccessibleContext().setAccessibleDescription("Save Roster Information and Move to Role Selection");

        text_field_array = new JTextField[23];
        text_field_array[0] = new JTextField(30);
        text_field_array[1] = new JTextField(3);
        for(int i = 2; i < 23; i++){
            text_field_array[i] = new JTextField(20);
        }
        label_array = new JLabel[24];

        label_array[0] = new JLabel(label_1_string + "New Team");
        constraints = CNC(0, 0, 6, 1, GridBagConstraints.NONE, 0,10,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(label_array[0], constraints);

        constraints = CNC(0, 1, 6, 1, GridBagConstraints.NONE, 0, 50,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(blank_label, constraints);

        label_array[1] = new JLabel("Team Name: ");
        label_array[2] = new JLabel("Team Abbreviation: ");
        constraints = CNC(1, 2, 1, 1, GridBagConstraints.NONE, 0,
                10, GridBagConstraints.EAST, default_inset);
        main_panel.add(label_array[1], constraints);
        constraints.gridx += 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridwidth += 1;
        main_panel.add(text_field_array[0], constraints);
        constraints.gridx += 2;
        constraints.gridwidth -= 1;
        main_panel.add(label_array[2], constraints);
        constraints.gridx += 1;
        constraints.anchor = GridBagConstraints.WEST;
        main_panel.add(text_field_array[1], constraints);

        for(int i = 0; i < 7; i++){
            label_array[3 + (3 * i)] = new JLabel("First Name: ");
            label_array[4 + (3 * i)] = new JLabel("Handle: ");
            label_array[5 + (3 * i)] = new JLabel("Last Name: ");

            constraints = CNC(0, 3 + i, 1, 1, GridBagConstraints.NONE, 0,
                    10, GridBagConstraints.CENTER, default_inset);
            main_panel.add(label_array[3 + (3 * i)], constraints);
            constraints.gridx += 1;
            main_panel.add(text_field_array[2 + (3 * i)], constraints);
            constraints.gridx += 1;
            main_panel.add(label_array[4 + (3 * i)], constraints);
            constraints.gridx += 1;
            main_panel.add(text_field_array[3 + (3 * i)], constraints);
            constraints.gridx += 1;
            main_panel.add(label_array[5 + (3 * i)], constraints);
            constraints.gridx += 1;
            main_panel.add(text_field_array[4 + (3 * i)], constraints);
        }

        constraints = CNC(0, 10, 6, 1, GridBagConstraints.NONE, 0,
                25, GridBagConstraints.CENTER, default_inset);
        main_panel.add(blank_label_2, constraints);
        constraints = CNC(4, 10, 1, 1, GridBagConstraints.NONE, 0,
                10, GridBagConstraints.CENTER, default_inset);
        main_panel.add(button_array[0], constraints);

        RefreshWindow();
    }

    public void UpdateTeam(){
        //TODO Build UpdateTeam Window
        /* Team Selection
         * Summon NewTeam then insert data or build from scratch?
         * Finish ExtendedListener handler
         */
        ClearWindow();
        main_panel.setLayout(new GridBagLayout());
        GridBagConstraints c;
        label_array = new JLabel[11];
        button_array = new JButton[12];
        int team_count = 0;
        Insets default_inset = new Insets(10, 5, 10, 5);

        label_array[0] = new JLabel("Choose a team to update or edit:");
        c = CNC(0, 0, 3, 1, GridBagConstraints.NONE, 0, 10,
                GridBagConstraints.CENTER, new Insets(10, 50, 10, 50));
        main_panel.add(label_array[0], c);

        while(team_count < 10 && (team_count + (10 * this.page_flag)) < data.team_array.length){

            label_array[team_count + 1] = new JLabel(data.team_array[team_count].team_name);
            c = CNC(0, team_count + 1, 2, 1, GridBagConstraints.NONE, 50,
                    10, GridBagConstraints.CENTER, default_inset);
            main_panel.add(label_array[team_count + 1], c);

            button_array[team_count] = new JButton("Update");
            button_array[team_count].addActionListener(handler);
            button_array[team_count].setActionCommand("push_team_update");
            c = CNC(2, team_count + 1, 1, 1, GridBagConstraints.NONE, 10,
                    5, GridBagConstraints.CENTER, default_inset);
            main_panel.add(button_array[team_count], c);

            team_count ++;
        }

        if(page_flag != 0){
            button_array[10] = new JButton("Previous Page");
            button_array[10].addActionListener(handler);
            button_array[10].setActionCommand("update_back_page");
            button_array[10].setMnemonic(KeyEvent.VK_P);
            c = CNC(0, team_count + 2, 1, 1, GridBagConstraints.NONE, 10,
                    10, GridBagConstraints.CENTER, default_inset);
            main_panel.add(button_array[10], c);
        }

        if((team_count + (10 * page_flag)) < data.team_array.length){
            button_array[11] = new JButton("Next Page");
            button_array[11].addActionListener(handler);
            button_array[11].setActionCommand("update_next_page");
            button_array[11].setMnemonic(KeyEvent.VK_N);
            c = CNC(2, team_count + 2, 1, 1, GridBagConstraints.NONE, 10,
                    10, GridBagConstraints.CENTER, default_inset);
            main_panel.add(button_array[11], c);
        }

        RefreshWindow();
    }

    public void DeleteTeam(){
        //TODO Build DeleteTeamWindow
        /* Team Selection
         * Confirmation message
         * Multiple deletions (BELLS AND WHISTLES)
         */
        ClearWindow();
        Insets default_inset = new Insets(5, 10, 5, 10);
        main_panel.setLayout(new GridBagLayout());
        label_array = new JLabel[11];
        button_array = new JButton[12];
        int team_count = 0;
        GridBagConstraints c;
        label_array[0] = new JLabel("Select a team to remove:");
        c = CNC(0, 0, 3, 1, GridBagConstraints.NONE, 50, 10,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(label_array[0], c);
        while(team_count < 10 && (team_count + (10 * page_flag)) < data.team_array.length){
            label_array[team_count + 1] = new JLabel(data.team_array[team_count + (10 * page_flag)].team_name);
            button_array[team_count] = new JButton("Delete Team");
            button_array[team_count].addActionListener(handler);
            button_array[team_count].setActionCommand("push_team_delete");
            c = CNC(0, 1 + team_count, 2, 1, GridBagConstraints.NONE, 25, 10,
                    GridBagConstraints.CENTER, default_inset);
            main_panel.add(label_array[team_count + 1], c);
            c.gridx = 2;
            c.gridwidth = 1;
            c.ipady = 5;
            main_panel.add(button_array[team_count], c);
            team_count ++;
        }
        if(page_flag > 0){
            button_array[10] = new JButton("Previous Page");
            button_array[10].addActionListener(handler);
            button_array[10].setActionCommand("delete_back_page");
            button_array[10].setMnemonic(KeyEvent.VK_P);

            c = CNC(0, 12, 1, 1, GridBagConstraints.NONE, 10, 10,
                    GridBagConstraints.CENTER, default_inset);
            main_panel.add(button_array[10], c);
        }
        if((team_count + (10 * page_flag)) < data.team_array.length){
            button_array[11] = new JButton("Next Page");
            button_array[11].addActionListener(handler);
            button_array[11].setActionCommand("delete_next_page");
            button_array[11].setMnemonic(KeyEvent.VK_N);

            c = CNC(2, 12, 1, 1, GridBagConstraints.NONE, 10, 10,
                    GridBagConstraints.CENTER, default_inset);
            main_panel.add(button_array[11], c);
        }

        RefreshWindow();
    }

    private void RefreshWindow(){
        main_frame.pack();
        main_panel.repaint();
        main_frame.setLocationRelativeTo(null);
    }

    private void ClearWindow(){
        ClearArrays();
        main_panel.removeAll();
        main_panel.setLayout(new GridBagLayout());
    }

    public void NewTeamPageTwo(){
        main_panel.removeAll();
        label_array = new JLabel[1 + (2 * active_edit_team.roster.length)];
        checkbox_array = new JCheckBox[5 * active_edit_team.roster.length];
        combo_array = new JComboBox[active_edit_team.roster.length];
        button_array = new JButton[2];
        GridBagConstraints constraints;
        Insets default_inset = new Insets(5, 10, 5, 10);
        constraints = CNC(0, 0, 8, 1, GridBagConstraints.NONE, 0, 10,
                GridBagConstraints.CENTER, default_inset);
        label_array[0] = new JLabel("Select roles and active role for each player on " + active_edit_team.team_name + ":");
        main_panel.add(label_array[0], constraints);

        for(int i = 0; i < active_edit_team.roster.length; i++){
            constraints = CNC(0, 1 + i, 1, 1, GridBagConstraints.NONE, 0, 10,
                    GridBagConstraints.CENTER, default_inset);
            label_array[1 + (2 * i)] = new JLabel("Select Available Roles for " + active_edit_team.roster[i].handle);
            main_panel.add(label_array[1 + (2 * i)], constraints);

            checkbox_array[5 * i] = new JCheckBox("Top");
            checkbox_array[5 * i].addItemListener(item_handler);
            constraints.gridx ++;
            main_panel.add(checkbox_array[5 * i], constraints);

            checkbox_array[(5 * i) + 1] = new JCheckBox("Jungle");
            checkbox_array[(5 * i) + 1].addItemListener(item_handler);
            constraints.gridx ++;
            main_panel.add(checkbox_array[(5 * i) + 1], constraints);

            checkbox_array[(5 * i) + 2] = new JCheckBox("Mid");
            checkbox_array[(5 * i) + 2].addItemListener(item_handler);
            constraints.gridx ++;
            main_panel.add(checkbox_array[(5 * i) + 2], constraints);

            checkbox_array[(5 * i) + 3] = new JCheckBox("Bot");
            checkbox_array[(5 * i) + 3].addItemListener(item_handler);
            constraints.gridx ++;
            main_panel.add(checkbox_array[(5 * i) + 3], constraints);

            checkbox_array[(5 * i) + 4] = new JCheckBox("Support");
            checkbox_array[(5 * i) + 4].addItemListener(item_handler);
            constraints.gridx ++;
            main_panel.add(checkbox_array[(5 * i) + 4], constraints);

            label_array[2 + (2 * i)] = new JLabel("Set Active Role:");
            constraints.gridx ++;
            main_panel.add(label_array[2 + (2 * i)], constraints);

            combo_array[i] = new JComboBox();
            combo_array[i].addItem("Bench");
            constraints.gridx ++;
            main_panel.add(combo_array[i], constraints);
        }

        button_array[0] = new JButton("Previous Page");
        button_array[0].addActionListener(handler);
        button_array[0].setActionCommand("new_team_two_back");
        button_array[0].setMnemonic(KeyEvent.VK_P);
        constraints = CNC(1, 9, 1, 1, GridBagConstraints.NONE, 0, 10,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(button_array[0], constraints);

        button_array[1] = new JButton("Finish Team");
        button_array[1].addActionListener(handler);
        button_array[1].setActionCommand("finish_new_team");
        button_array[1].setMnemonic(KeyEvent.VK_F);
        constraints.gridx += 5;
        main_panel.add(button_array[1], constraints);

        RefreshWindow();
    }

    private void ClearArrays(){
        label_array = null;
        button_array = null;
        text_field_array = null;
        checkbox_array = null;
        combo_array = null;
        auto_array = null;
    }

    private void DataWrite(){
        for(int i = 0; i < data.team_array.length; i++){
            String[] temp = data.team_array[i].Stringify();
            try {
                io.PushStrings(temp, IO.TEAM);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < data.champ_array.length; i++){
            String[] temp = data.champ_array[i].Stringify();
            try{
                io.PushStrings(temp, IO.CHAMP);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    private void RunStartUp(){
        try {
            io.ReadTeamData();
            io.ReadChampData();
        } catch (IOException e) {
            e.printStackTrace();
            io.CloseThreads();
            System.exit(-2);
        }


    }

    public void ChampSelect(){
        ClearWindow();
        main_panel.setLayout(new GridBagLayout());
        label_array = new JLabel[3];
        label_array[0] = new JLabel("Select Which Teams Are Playing:");
        label_array[1] = new JLabel("Blue Team:");
        label_array[2] = new JLabel("Red Team:");

        combo_array = new JComboBox[2];
        combo_array[0] = new JComboBox();
        combo_array[1] = new JComboBox();
        for(int i = 0; i < data.team_array.length; i++){
            combo_array[0].addItem(data.team_array[i].team_name);
            combo_array[1].addItem(data.team_array[i].team_name);
        }

        button_array = new JButton[1];
        button_array[0] = new JButton("Next Page");
        button_array[0].setActionCommand("champ_select_teams");
        button_array[0].addActionListener(handler);
        button_array[0].setMnemonic(KeyEvent.VK_N);
        Insets default_inset = new Insets(5, 10, 5, 10);
        GridBagConstraints c = CNC(0, 0, 3, 1, GridBagConstraints.NONE, 10,
                10, GridBagConstraints.CENTER, default_inset);
        main_panel.add(label_array[0], c);

        c = CNC(0, 1, 1, 1, GridBagConstraints.NONE, 10, 10,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(label_array[1], c);
        c.gridx += 2;
        main_panel.add(label_array[2], c);

        c.gridy += 1;
        c.gridx -= 2;
        main_panel.add(combo_array[0], c);
        c.gridx += 2;
        main_panel.add(combo_array[1], c);

        c = CNC(1, 3, 2, 1, GridBagConstraints.NONE, 10, 10,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(button_array[0], c);


        RefreshWindow();
    }

    public void ChampSelectPickBan(){
        champ_sel_flag = 0;
        List<String> champ_names = new ArrayList<String>();
        for(int i = 0; i < data.champ_array.length; i++){
            champ_names.add(data.champ_array[i].name);
        }
        s = new StringSearchable(champ_names);
        /*label_array assignments
         *0 -> Current Ban/Pick
         *1 -> Current Ban/Pick Info
         *2 -> Blue Bans
         *3 -> Red Bans
         *4-8 -> Blue Team Names
         *9-13 -> Red Team Names
         *14-18 -> Blue Team Champs
         *19-23 -> Red Team Champs
         *24-28 -> Blue Team Bans
         *29-33 -> Red Team Bans
         *34 -> Blue Team Picks
         *35 -> Red Team Picks
         *36 -> Blue Team Name
         *37 -> Red Team Name
         */
        ClearWindow();

        main_panel.setLayout(new GridBagLayout());
        label_array = new JLabel[38];
        auto_array = new AutocompleteJComboBox[2];
        button_array = new JButton[1];
        Insets default_inset = new Insets(5, 10, 5, 10);

        button_array[0] = new JButton("Confirm Pick/Ban");
        button_array[0].setMnemonic(KeyEvent.VK_C);
        button_array[0].addActionListener(champ_handler);
        button_array[0].setActionCommand("champ_select_continue");
        label_array[0] = new JLabel("Current Ban:");
        label_array[1] = new JLabel("Blue 1");
        label_array[2] = new JLabel("Blue Team Bans:");
        label_array[3] = new JLabel("Red Team Bans:");
        for(int i = 0; i < data.blue_team.roster.length; i++){
            switch(data.blue_team.roster[i].active_role){
                case 1:
                    label_array[4] = new JLabel(data.blue_team.roster[i].handle);
                    break;
                case 2:
                    label_array[5] = new JLabel(data.blue_team.roster[i].handle);
                    break;
                case 4:
                    label_array[6] = new JLabel(data.blue_team.roster[i].handle);
                    break;
                case 8:
                    label_array[7] = new JLabel(data.blue_team.roster[i].handle);
                    break;
                case 16:
                    label_array[8] = new JLabel(data.blue_team.roster[i].handle);
                    break;
                default:
                    break;
            }
        }

        for(int i = 0; i < data.red_team.roster.length; i++){
            switch(data.red_team.roster[i].active_role){
                case 1:
                    label_array[9] = new JLabel(data.red_team.roster[i].handle);
                    break;
                case 2:
                    label_array[10] = new JLabel(data.red_team.roster[i].handle);
                    break;
                case 4:
                    label_array[11] = new JLabel(data.red_team.roster[i].handle);
                    break;
                case 8:
                    label_array[12] = new JLabel(data.red_team.roster[i].handle);
                    break;
                case 16:
                    label_array[13] = new JLabel(data.red_team.roster[i].handle);
                    break;
                default:
                    break;
            }
        }

        for(int i = 14; i < 34; i++){
            label_array[i] = new JLabel("");
        }
        label_array[34] = new JLabel("Blue Team Picks:");
        label_array[35] = new JLabel("Red Team Picks:");
        label_array[36] = new JLabel(data.blue_team.team_name);
        label_array[37] = new JLabel(data.red_team.team_name);

        for(int i = 0; i < 2; i++){
            auto_array[i] = new AutocompleteJComboBox(s);
        }

        GridBagConstraints c = CNC(3, 0, 1, 1, GridBagConstraints.NONE, 5,
                10, GridBagConstraints.CENTER, default_inset);

        main_panel.add(label_array[0], c);
        c.gridx ++;
        main_panel.add(label_array[1], c);

        c = CNC(0, 0, 1, 1, GridBagConstraints.NONE, 10, 10,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(label_array[2], c);
        for(int i = 0; i < 5; i++){
            c.gridy ++;
            main_panel.add(label_array[24 + i], c);
        }
        c.gridx += 7;
        c.gridy -= 5;
        main_panel.add(label_array[3], c);
        for(int i = 0; i < 5; i++){
            c.gridy++;
            main_panel.add(label_array[29 + i], c);
        }


        c = CNC(1, 1, 1, 1, GridBagConstraints.NONE, 10, 10,
                GridBagConstraints.CENTER, default_inset);

        for(int i = 0; i < 5; i++){
            main_panel.add(label_array[4 + i], c);
            c.gridy ++;
        }
        c.gridy -= 5;
        c.gridx ++;
        for(int i = 0; i < 5; i++){
            main_panel.add(label_array[14 + i], c);
            c.gridy++;
        }
        c.gridy -= 5;
        c.gridx += 4;
        for(int i = 0; i < 5; i++){
            main_panel.add(label_array[9 + i], c);
            c.gridy ++;
        }
        c.gridy -= 5;
        c.gridx --;
        for(int i = 0; i < 5; i++){
            main_panel.add(label_array[19 + i], c);
            c.gridy ++;
        }
        c = CNC(3, 3, 1, 1, GridBagConstraints.NONE, 10, 10,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(auto_array[0], c);
        c.gridx ++;
        main_panel.add(auto_array[1], c);
        auto_array[1].setEditable(false);

        c = CNC(3, 5, 2, 1, GridBagConstraints.NONE, 10, 10,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(button_array[0], c);

        c = CNC(2, 0, 1, 1, GridBagConstraints.NONE, 10, 10,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(label_array[34], c);
        c.gridx += 3;
        main_panel.add(label_array[35], c);

        c = CNC(1, 0, 1, 1, GridBagConstraints.NONE, 10, 10,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(label_array[36], c);
        c.gridx += 5;
        main_panel.add(label_array[37], c);

        RefreshWindow();
    }

    public void PublicRefresh(){
        RefreshWindow();
    }

    public void ChampSelectSwaps(){
        main_panel.remove(auto_array[0]);
        main_panel.remove(auto_array[1]);
        main_panel.remove(button_array[0]);
        button_array[0] = new JButton("Submit Swap(s)");
        button_array[0].addActionListener(champ_handler);
        button_array[0].setActionCommand("champ_select_swap");
        label_array[0].setText("Select Champion Trades");
        label_array[1].setText("");
        auto_array = null;
        combo_array = new JComboBox[10];
        GridBagConstraints c = CNC(3, 1, 1, 1, GridBagConstraints.NONE, 10,
                10, GridBagConstraints.CENTER, new Insets(5,10,5,10));
        for(int i = 0; i < 10; i++){
            combo_array[i] = new JComboBox();
            if(i / 5 == 1){
                data.ListFill(i - 5, DataHandler.RED);
            }
            else{
                data.ListFill(i, DataHandler.BLUE);
            }
        }
        for(int i = 0; i < 5; i++){
            main_panel.add(combo_array[i], c);
            c.gridy++;
        }
        c.gridy -= 5;
        c.gridx ++;

        for(int i = 0; i < 5; i++){
            main_panel.add(combo_array[5 + i], c);
            c.gridy++;
        }
        c = CNC(4, 7, 2, 1, GridBagConstraints.NONE, 10, 10,
                GridBagConstraints.CENTER, new Insets(5, 10, 5, 10));
        main_panel.add(button_array[0], c);
        RefreshWindow();
    }
    //TODO File I/O
    /* File Write
     * File Read
     * File Tree Browsing? (BELLS AND WHISTLES)
     */
    //TODO Champ Info
    /* Champ Struct
     * Searchbars for Champ Select? (BELLS AND WHISTLES)
     */
    //TODO Info Storage
    /* Clean up structs
     * Struct read/write/storage
     */
    //TODO Display Info
    /* Multithreading? (Research)
     * Second Window... Build into TLR or create new?
     * Build visuals
     */
    //TODO Image Handling
    /* Image types?
     * Need special handler or can use built-ins?
     */
}
