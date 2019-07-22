import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/* This is the main class of the program, and the one which houses the main interface for controlling.
 * At no point during execution of the program should this class be completely inactive.
 * In comments, this class will be abbreviated as TLR.
 */

public class TopLevelRewrite {
    /* An assortment of variables internal to TLR and some which refer to instances of other classes.
     * Some class references are mirrored in other classes, based on whether it was worth the effort of
     * passing it in to save on typing references to classes through the TLR instance.
     * All classes will have their functionality explained in their own header in their source code.
     */
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
    StatsInterface si;
    APIData api;
    static CustomSplash splash;
    int page_flag;
    int loading_flag;
    int champ_sel_flag;
    
    public TopLevelRewrite(){
        /* Building the frame for the main user interface. The standard window close event is overwritten here
         * in order to always ensure data is written to permanent memory prior to end of execution.
         *
         * In all frames in this project, GridBagLayout is used for its versatility and organization.
         * Furthermore, the JFrame library and its components are made extensive use of. Most of these components
         * are fairly self-explanatory (JButtons are buttons, JLabels are static text, etc.), so they will not be
         * explained except in extenuating circumstances.
         * All JFrame components are stored in the various arrays of TLR so that outside classes can access, read
         * and modify their values.
         */
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
    }

    public static void main(String[] args) throws IOException {
        /* The execution header for the program. Before any other functions are called, the program retrieves the
         * current version of DDragon, Riot's static data storage locale. This is because start-up functionality for
         * champion data requires their champion ID, which is pulled from DDragon in order to future-proof against
         * ID changes.
         *
         * The rest of main() consists of initializing the various classes required by TLR and running various
         * other subroutines to pull saved data and clean out memory (technically unnecessary, but a habit from
         * writing in C). As with classes, the subroutines will be explained at their declaration.
         */
        splash = new CustomSplash();
        splash.ShowSplash();
        RiotAPICall.GetCurrentVersion();
        TopLevelRewrite new_window = new TopLevelRewrite();
        new_window.io = new IO(new_window);
        new_window.data = new DataHandler(new_window);
        splash.ChangeText("Reading Team Data");
        new_window.RunStartUp();
        splash.ChangeText("Building Components");
        new_window.handler = new ExtendedListener(new_window);
        new_window.item_handler = new ItemListenerEXT(new_window);
        new_window.champ_handler = new ChampSelectListener(new_window);
        new_window.loading_flag = 0;
        new_window.champ_sel_flag = 0;
        new_window.page_flag = 0;
        new_window.screen = new DisplayWindow(new_window, new_window.data);
        new_window.si = new StatsInterface(new_window.screen);
        new_window.api = new APIData(new_window);
        new_window.InitMenus();
        new_window.PopulateLander();
        splash.EndSplash();
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new_window.StartWindow();
            }
        });
        RiotAPICall.SetKey(new_window.GetAPIKey());
    }

    public void PopulateLander(){
        /* This creates the landing screen for the program. After any action is complete or cancelled, the
         * program returns here.
         */
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
        /* Function to build the menu bar for TLR's frame. All of these options link to handler, an instance of
         * ExtendedListener which handles most user interaction. The origin and destination of each call will be
         * documented there.
         */
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

        temp_item = new JMenuItem("View Team Stats");
        temp_item.setActionCommand("stats_team");
        temp_item.addActionListener(handler);
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
        temp_menu = new JMenu("Stats Window");

        temp_item = new JMenuItem("Champion Stats");
        temp_item.setActionCommand("champ_stats_main");
        temp_item.addActionListener(handler);
        temp_menu.add(temp_item);

        build_bar.add(temp_menu);

        temp_item = new JMenuItem("API Key");
        temp_item.addActionListener(handler);
        temp_item.setActionCommand("api_key_input");
        build_bar.add(temp_item);

        temp_item = new JMenuItem("End Game");
        temp_item.addActionListener(handler);
        temp_item.setActionCommand("current_game_end");
        build_bar.add(temp_item);

        temp_item = new JMenuItem("Test");
        temp_item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataLoadingScreen();
                Thread thread = new Thread(){
                    public void run(){
                        try{
                            Thread.sleep(1000);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        api.PostGameHandler();
                    }
                };
                thread.start();
            }
        });
        build_bar.add(temp_item);

        main_frame.setJMenuBar(build_bar);
    }

    private GridBagConstraints CNC(int xloc, int yloc, int xsize, int ysize, int fill, int intpadx,
                                                           int intpady, int anchor, Insets inset){
        /* An ease-of-use program to make building GridBagConstraints less space-consuming. Takes inputs for
         * each of the variables in a GBC, and properly assigns them. Returns a fully-built GBC.
         */
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
        //Declared here for each of comprehension in main().
        main_frame.setVisible(true);
    }

    public void NewTeam(){
        /* The team creation interface. This particular window contains input space for team name, team abbreviation,
         * and up to 7 team members.
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
        /* The team edit interface. Offers up to 10 teams from memory to choose from, with teams unable to be shown
         * offered on subsequent pages. Page number is stored in the page_flag TLR variable, 0-indexed.
         * Upon selection of a team, UpdateTeam()'s handler calls NewTeam() and fills it in with the stored
         * data from that team.
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
        /* A helper function called at the end of all interface-building functions. It resizes, redraws and
         * re-centers the interface window.
         */
        main_frame.pack();
        main_panel.validate();
        main_frame.setLocationRelativeTo(null);
    }

    private void ClearWindow(){
        /* A helper function called at the beginning of all interface-building functions. It empties the TLR JFrame
         * component arrays to free memory for garbage collection, removes all items from the interface window,
         * and resets the layout to a clean GridBagLayout for the new interface.
         */
        ClearArrays();
        main_panel.removeAll();
        main_panel.setLayout(new GridBagLayout());
    }

    public void NewTeamPageTwo(){
        /* The second window of the new team functionality. This window allows selection of which roles each player
         * entered on the previous screen is capable of filling, as well as selection of which role they currently
         * fill. The handler ensures that a user cannot advance past this window without choosing a player for all 5
         * standard roles.
         */
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
        // A helper function for ClearWindow() to enable automatic garbage collection for old JFrame components.
        label_array = null;
        button_array = null;
        text_field_array = null;
        checkbox_array = null;
        combo_array = null;
        auto_array = null;
    }

    private void DataWrite(){
        /* An end-of-runtime function which takes all active-memory data concerning teams and champions and sends it
         * to inter-execution storage. Because of access rights, it is not recommended to run multiple instances of
         * this program at the same time for data integrity.
         * All data is stored in (root)/data, as .txt files.
         */
        for(int i = 0; i < data.team_array.length; i++){
            for(int j = 0; j < data.team_array[i].roster.length; j++){
                io.WritePlayerData(data.team_array[i].team_abbr, data.team_array[i].roster[j]);
            }
            String[] temp = data.team_array[i].Stringify();
            io.WriteTeamStatData(data.team_array[i].StringifyStats(), data.team_array[i].team_abbr);
            try {
                io.PushStrings(temp, IO.TEAM);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] games_tot = {Integer.toString(data.total_games)};
        try {
            io.PushStrings(games_tot, IO.CHAMP);
        }
        catch (IOException e){
            e.printStackTrace();
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
        /* A helper function to read previously-stored data. If data is corrupt, the program closes with exit code
         * -2. This should never happen as the IO class has protections in place, but the safeguard is here
         * nonetheless.
         */
        try {
            io.ReadTeamData();
            splash.ChangeText("Reading Champion Data");
            io.ReadChampData();
            splash.ChangeText("Checking For New Champs");
            Champ temp = DataHandler.CheckChamps(data.champ_array);
            if(temp != null){
                int length = data.champ_array.length;
                Champ[] temp_array = new Champ[length + 1];
                System.arraycopy(data.champ_array, 0, temp_array, 0, data.champ_array.length);
                temp_array[length] = temp;
                data.champ_array = temp_array;
            }
        } catch (IOException e) {
            e.printStackTrace();
            io.CloseThreads();
            System.exit(-2);
        }


    }

    public void ChampSelect(){
        /* The precursor to the champ select pick/ban window. This interface allows the operator to select the two
         * teams which will be competing in the draft to follow.
         */
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
        /* The heart of the champ select functionality (and the original intention) of this program. This interface
         * allows the operator to select champions for each portion of the pick/ban cycle. Previous selections are
         * displayed in plain text in the interface, and each selection updates TLR's child DisplayWindow (screen)
         * with the proper graphics.
         */
        champ_sel_flag = 0;
        List<String> champ_names = new ArrayList<String>();
        for(int i = 0; i < data.champ_array.length; i++){
            champ_names.add(data.champ_array[i].name);
        }
        //StringSearchable is the base for a custom JFrame component, explained in detail in its class declaration.
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
            //AutocompleteJComboBox is the custom JFrame component, explained properly in its declaration.
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
        TimerThread t = new TimerThread(40, screen);
        screen.SetThread(t);
        t.Trigger();
        RefreshWindow();
    }

    public void PublicRefresh(){
        //A public wrapper for refreshing TLR's main frame.
        RefreshWindow();
    }

    public void ChampSelectSwaps(){
        /* Interface for the operator to reassign champs to their proper player after the draft phase has ended.
         * Due to limits on the JFrame components, this is done by selecting new locations and pressing submit,
         * but may be done multiple times while the post-draft timer ticks.
         */
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

    private String GetAPIKey() {
        //Pulls the API key for Riot's API from a .txt file. Not stored in-source for ease of updating.
        String temp = null;
        try {
            BufferedReader APIReader = new BufferedReader(new FileReader(".\\data\\static\\api_key.txt"));
            temp = APIReader.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        if(temp == null){
            JOptionPane.showMessageDialog(null, "API Key not found or unreadable. Please use the API Key option to input a key before using data retrieval.");
        }
        return temp;
    }

    public void APIKeyInput(){
        ClearWindow();
        label_array = new JLabel[3];
        button_array = new JButton[1];
        text_field_array = new JTextField[1];
        label_array[0] = new JLabel("The current API key is:");
        label_array[1] = new JLabel(RiotAPICall.api_key);
        label_array[2] = new JLabel("Enter the new API key below:");
        button_array[0] = new JButton("Set API Key");
        button_array[0].addActionListener(handler);
        button_array[0].setActionCommand("api_key_submit");
        text_field_array[0] = new JTextField(25);

        GridBagConstraints c;

        c = CNC(0, 0, 1, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.CENTER,
                new Insets(5, 5,5, 5));
        main_panel.add(label_array[0], c);
        c.gridy ++;
        main_panel.add(label_array[1], c);
        c.gridy ++;
        main_panel.add(label_array[2], c);
        c.gridy ++;
        main_panel.add(text_field_array[0], c);
        c.gridy ++;
        main_panel.add(button_array[0], c);
        RefreshWindow();
    }

    public void TeamStatsSelect(){
        ClearWindow();
        Insets default_inset = new Insets(5, 10, 5, 10);
        main_panel.setLayout(new GridBagLayout());
        label_array = new JLabel[11];
        button_array = new JButton[12];
        int team_count = 0;
        GridBagConstraints c;
        label_array[0] = new JLabel("Select a team to view:");
        c = CNC(0, 0, 3, 1, GridBagConstraints.NONE, 50, 10,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(label_array[0], c);
        while(team_count < 10 && (team_count + (10 * page_flag)) < data.team_array.length){
            label_array[team_count + 1] = new JLabel(data.team_array[team_count + (10 * page_flag)].team_name);
            button_array[team_count] = new JButton("Select Team");
            button_array[team_count].addActionListener(handler);
            button_array[team_count].setActionCommand("push_team_stats");
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
            button_array[10].setActionCommand("stats_back_page");
            button_array[10].setMnemonic(KeyEvent.VK_P);

            c = CNC(0, 12, 1, 1, GridBagConstraints.NONE, 10, 10,
                    GridBagConstraints.CENTER, default_inset);
            main_panel.add(button_array[10], c);
        }
        if((team_count + (10 * page_flag)) < data.team_array.length){
            button_array[11] = new JButton("Next Page");
            button_array[11].addActionListener(handler);
            button_array[11].setActionCommand("stats_next_page");
            button_array[11].setMnemonic(KeyEvent.VK_N);

            c = CNC(2, 12, 1, 1, GridBagConstraints.NONE, 10, 10,
                    GridBagConstraints.CENTER, default_inset);
            main_panel.add(button_array[11], c);
        }
        RefreshWindow();
    }

    public void DisplayTeamStats(Team t){
        ClearWindow();
        Insets default_insets = new Insets(5,5,5,5);
        label_array = new JLabel[15];
        combo_array = new JComboBox[1];
        label_array[0] = new JLabel("Stats for " + t.team_name + ":");
        label_array[1] = new JLabel("Wins: " + t.GetWins());
        label_array[2] = new JLabel("Losses: " + t.GetLosses());
        if(t.GetKDA() != -1) {
            label_array[3] = new JLabel("Team KDA: " + String.format("%.2f", t.GetKDA()));
        }
        else{
            label_array[3] = new JLabel("Team KDA: Perfect");
        }
        label_array[4] = new JLabel("Team Gold Per Game: " + String.format("%.2f", t.GetGold()));
        label_array[5] = new JLabel("Team Inhibs Per Game: " + String.format("%.2f", t.GetInhibs()));
        label_array[6] = new JLabel("Team Towers Per Game: " + String.format("%.2f", t.GetTowers()));
        label_array[7] = new JLabel("Barons Per Game: " + String.format("%.2f", t.GetBarons()));
        label_array[8] = new JLabel("Dragons Per Game: " + String.format("%.2f", t.GetDrags()));
        label_array[9] = new JLabel("Heralds Taken: " + t.GetHerald());
        label_array[10] = new JLabel("First Dragon Rate: " + String.format("%.2f", t.GetFDRate()));
        label_array[11] = new JLabel("Enemy Barons Per Game: " + String.format("%.2f", t.GetEnemyBarons()));
        label_array[12] = new JLabel("Enemy Dragons Per Game: " + String.format("%.2f", t.GetEnemyDrags()));
        label_array[13] = new JLabel("Enemy Heralds Taken: " + t.GetEnemyHeralds());
        label_array[14] = new JLabel("Select a player to view individual stats:");

        combo_array[0] = new JComboBox();
        for(int i = 0; i < t.roster.length; i++){
            combo_array[0].addItem(t.roster[i].handle);
        }
        combo_array[0].addActionListener(handler);
        combo_array[0].setActionCommand("select_player_stat");

        GridBagConstraints c = CNC(0, 0, 4, 1, GridBagConstraints.NONE, 5, 5,
                GridBagConstraints.CENTER, default_insets);
        main_panel.add(label_array[0], c);

        c = CNC(0, 1, 1, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.WEST,
                default_insets);
        main_panel.add(label_array[1], c);
        c.gridx ++;
        main_panel.add(label_array[2], c);
        c.gridx ++;
        main_panel.add(label_array[3], c);

        c = CNC(0, 2, 1, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.WEST,
                default_insets);
        main_panel.add(label_array[4], c);
        c.gridx ++;
        main_panel.add(label_array[5], c);
        c.gridx ++;
        main_panel.add(label_array[6], c);

        c = CNC(0, 3, 1, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.WEST,
                default_insets);
        main_panel.add(label_array[7], c);
        c.gridx ++;
        main_panel.add(label_array[8], c);
        c.gridx ++;
        main_panel.add(label_array[9], c);

        c = CNC(0, 4, 1, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.WEST,
                default_insets);
        main_panel.add(label_array[11], c);
        c.gridx ++;
        main_panel.add(label_array[12], c);
        c.gridx ++;
        main_panel.add(label_array[13], c);

        c = CNC(1, 5, 1, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.WEST,
                default_insets);
        main_panel.add(label_array[10], c);

        c = CNC(0, 6, 2, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.CENTER,
                default_insets);
        main_panel.add(label_array[14], c);
        c.gridwidth = 1;
        c.gridx = 2;
        main_panel.add(combo_array[0], c);

        RefreshWindow();
    }

    public void DisplayPlayerStats(Player p){
        ClearWindow();
        Insets default_inset = new Insets(5,5,5,5);
        label_array = new JLabel[10];
        button_array = new JButton[1];

        label_array[0] = new JLabel("Stats for " + p.first_name + " \"" + p.handle + "\" " + p.last_name + ":");
        label_array[1] = new JLabel("Wins: " + p.win);
        label_array[2] = new JLabel("Losses: " + p.loss);
        if(p.GetKDA() != -1) {
            label_array[3] = new JLabel("KDA: " + String.format("%.2f",p.GetKDA()));
        }
        else{
            label_array[3] = new JLabel("KDA: Perfect");
        }
        label_array[4] = new JLabel("CSD @ 10 Min: " + String.format("%.2f",p.CSD10));
        label_array[5] = new JLabel("CS/Min: " + String.format("%.2f", p.CSmin));
        label_array[6] = new JLabel("Vision Score: " + String.format("%.2f", p.vis));
        label_array[7] = new JLabel("Neutral Monsters Killed: " + String.format("%.2f", p.neutrals));
        label_array[8] = new JLabel("Enemy Neutrals Killed: " + String.format("%.2f", p.neutral_enemy));
        label_array[9] = new JLabel("Gold Per Game: " + String.format("%.2f", p.gold));

        button_array[0] = new JButton("Return to Team Stats");
        button_array[0].addActionListener(handler);
        button_array[0].setActionCommand("stat_player_return");

        GridBagConstraints c = CNC(0, 0, 3, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.CENTER,
               default_inset);
        main_panel.add(label_array[0], c);

        c = CNC(0, 1, 1, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.WEST,
                default_inset);
        main_panel.add(label_array[1], c);
        c.gridx++;
        main_panel.add(label_array[2], c);
        c.gridx++;
        main_panel.add(label_array[3], c);

        c = CNC(0, 2, 1, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.WEST,
                default_inset);
        main_panel.add(label_array[4], c);
        c.gridx ++;
        main_panel.add(label_array[5], c);
        c.gridx ++;
        main_panel.add(label_array[6], c);

        c = CNC(0, 3, 1, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.WEST,
                default_inset);
        main_panel.add(label_array[7], c);
        c.gridx++;
        main_panel.add(label_array[8], c);
        c.gridx ++;
        main_panel.add(label_array[9], c);

        c = CNC(0, 4, 1, 1, GridBagConstraints.NONE, 5, 5, GridBagConstraints.EAST,
                default_inset);
        main_panel.add(button_array[0], c);

        RefreshWindow();
    }

    public void DataLoadingScreen(){
        ClearWindow();
        label_array = new JLabel[2];
        label_array[0] = new JLabel("Accessing Riot game data...");
        label_array[1] = new JLabel(new ImageIcon(".\\img\\loadbar.gif"));
        GridBagConstraints c = CNC(0,0, 1, 1, GridBagConstraints.NONE, 5, 5,
                GridBagConstraints.CENTER, new Insets(5,5,5,5));
        main_panel.add(label_array[0], c);
        c.gridy ++;
        main_panel.add(label_array[1], c);
        RefreshWindow();
    }

    public void ChampStatsLander(){
        ClearWindow();
        label_array = new JLabel[1];
        button_array = new JButton[1];
        auto_array = new AutocompleteJComboBox[1];

        List<String> champ_names = new ArrayList<String>();
        for(int i = 0; i < data.champ_array.length; i++){
            champ_names.add(data.champ_array[i].name);
        }

        s = new StringSearchable(champ_names);
        auto_array[0] = new AutocompleteJComboBox(s);

        label_array[0] = new JLabel("Select a champion and click \"Submit\" to view that champion's stats:");

        button_array[0] = new JButton("Submit");
        button_array[0].addActionListener(handler);
        button_array[0].setActionCommand("champ_stats_submit");

        GridBagConstraints c = CNC(0, 0, 1, 1, GridBagConstraints.NONE, 5, 5,
                GridBagConstraints.CENTER, new Insets(5,5,5,5));
        main_panel.add(label_array[0], c);
        c.gridy ++;
        main_panel.add(auto_array[0], c);
        c.gridy ++;
        main_panel.add(button_array[0], c);
        RefreshWindow();
    }

    public void ChampStatsDisplay(Champ target){
        ClearWindow();
        Insets default_inset = new Insets(5,5,5,5);
        label_array = new JLabel[7];
        button_array = new JButton[1];

        button_array[0] = new JButton("Return to List");
        button_array[0].setActionCommand("champ_stats_return");
        button_array[0].addActionListener(handler);

        label_array[0] = new JLabel("Stats for " + target.name + ":");
        label_array[1] = new JLabel("Pick Rate: " + String.format("%.2f", (100 * target.GetPickRate())) + "% (" +
                target.GetPicks() + "/" + DataHandler.total_games + ")");
        label_array[2] = new JLabel("Ban Rate: " + String.format("%.2f", (100 * target.GetBanRate())) + "% (" +
                target.GetBans() + "/" + DataHandler.total_games + ")");
        label_array[3] = new JLabel("Win Rate: " + String.format("%.2f", (100 * target.GetWinRate())) + "% (" +
                target.GetWins() + "/" + target.GetPicks() + ")");
        label_array[4] = new JLabel("Presence: " + String.format("%.2f", (100 * target.GetPresence())) + "% (" +
                target.GetPresenceInt() + "/" + DataHandler.total_games + ")");
        label_array[5] = new JLabel("Average KDA: " + String.format("%.2f", target.GetKDA()));
        label_array[6] = new JLabel("Average CS/D @ 10: " + String.format("%.2f", target.GetCSD()));

        GridBagConstraints c = CNC(0, 0, 2, 1, GridBagConstraints.NONE, 5, 5,
                GridBagConstraints.CENTER, default_inset);
        main_panel.add(label_array[0], c);

        c = CNC(0, 1, 1, 1, GridBagConstraints.NONE, 5, 5,
                GridBagConstraints.WEST, default_inset);
        main_panel.add(label_array[4], c);
        c.gridx ++;
        main_panel.add(label_array[1], c);
        c.gridx = 0;
        c.gridy ++;
        main_panel.add(label_array[2], c);
        c.gridx ++;
        main_panel.add(label_array[3], c);
        c.gridx = 0;
        c.gridy ++;
        main_panel.add(label_array[5], c);
        c.gridx ++;
        main_panel.add(label_array[6], c);

        c = CNC(1, 4, 1, 1, GridBagConstraints.NONE, 5, 5,
                GridBagConstraints.EAST, default_inset);
        main_panel.add(button_array[0], c);
        RefreshWindow();
    }


    //TODO Display Info
    /* Build visuals
     */

    //TODO Champ portraits
    /* Pull from ddragon
     * Determine crop points
     * Champion orientation
     */

    //TODO Art builder
    /* Write onto Green Screen
     * Graphics objects and bufferedimage items
     * Slanted Rectangles? Masks? Layering?
     */

    //TODO art assets
    /* Get from serafina */

    //TODO stat window
    /* art from Serafina
     * Interface -- build into TLR
     */
}
