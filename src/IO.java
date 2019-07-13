import javax.swing.*;
import java.io.*;
import java.nio.Buffer;
import java.nio.file.Path;

public class IO {
    /*Data IO class for the program. This class reads saved data into the DataHandler instance and writes given data
     * out to new files. Buffered I/O offers some multi-threading/multi-application safety, but should not be
     * bet on.
     */
    TopLevelRewrite window;
    BufferedReader team_buffered_old;
    BufferedWriter team_buffered;
    BufferedReader champ_buffered_old;
    BufferedWriter champ_buffered;

    int new_flag;
    static final int TEAM = 10;
    static final int CHAMP = 20;

    public IO(TopLevelRewrite main_window) throws IOException{
        //Attempts to open saved data and new temporary files. If it fails, opens fresh files instead.
        try {
            team_buffered_old = new BufferedReader(new FileReader(".\\data\\team_data.dat"));
            team_buffered = new BufferedWriter(new FileWriter(".\\data\\team_data_temp.dat"));
            champ_buffered_old = new BufferedReader(new FileReader(".\\data\\champ_data.dat"));
            champ_buffered = new BufferedWriter(new FileWriter(".\\data\\champ_data_temp.dat"));
        }
        catch(IOException e){
            team_buffered = new BufferedWriter(new FileWriter(".\\data\\team_data.dat"));
            ChampList.main2(null);
            champ_buffered_old = new BufferedReader(new FileReader(".\\data\\champ_data.dat"));
            champ_buffered = new BufferedWriter(new FileWriter(".\\data\\champ_data_temp.dat"));
        }
        window = main_window;
    }

    public void PushStrings(String[] strings, int type_flag) throws IOException{
        /* Simple writer. Writes given strings to either the team or champion data file, split by newlines.
         * Near Future: Extension to write individual data files for player data. Coming with Riot API upgrade.
         */
        //FIXME player data output
        switch(type_flag){
            case TEAM:
                for(int i = 0; i < strings.length; i++){
                    team_buffered.write(strings[i]);
                    team_buffered.write("\n");
                }
                break;
            case CHAMP:
                for(int i = 0; i < strings.length; i++){
                    champ_buffered.write(strings[i]);
                    champ_buffered.write("\n");
                }
                break;
            default:
                break;
        }
        team_buffered.flush();
    }

    public void CloseThreads(){
        //Simple cleanup. Closes threads at the end of writing, and calls ReplaceOldFiles() to overwrite old data with new.
        try {
            team_buffered.close();
            champ_buffered.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ReplaceOldFiles();
    }

    private void ReplaceOldFiles(){
        //Deletes old saved data and overwrites it with the newly written information.
        File old_team_file = new File(".\\data\\team_data.dat");
        File new_team_file = new File(".\\data\\team_data_temp.dat");
        old_team_file.delete();
        new_team_file.renameTo(old_team_file);
        old_team_file = new File(".\\data\\champ_data.dat");
        new_team_file = new File(".\\data\\champ_data_temp.dat");
        old_team_file.delete();
        new_team_file.renameTo(old_team_file);
    }

    public void ReadTeamData() throws IOException{
        /*Reads in team data from /data/team_data.dat. Loops until EOF, as all team entries are identical in format.
         * "---" is used to denote separation between team entries, so the program understands the possibility of
         * less than 7 players per team.
         */
        String buffer;
        buffer = team_buffered_old.readLine();
        String[] array = new String[40];
        int i = 0;
        while(buffer != null && !(buffer.equals(""))){
            while(!(buffer.equals("---"))){
                array[i] = buffer;
                i++;
                buffer = team_buffered_old.readLine();
            }
            String[] temp_array = new String[i];
            System.arraycopy(array, 0, temp_array, 0, i);
            Team temp_team = Team.Destringify(temp_array);
            for(int j= 0; j < temp_team.roster.length; j++){
                ReadPlayerData(temp_team.team_abbr, temp_team.roster[j]);
            }
            window.data.AddTeam(temp_team);
            i = 0;
            array = new String[40];
            buffer = team_buffered_old.readLine();
        }
        team_buffered_old.close();

    }

    public void ReadChampData() throws IOException{
        /*Reads in champ data from /data/champ_data.dat. Loops over champions, each of which has four lines of info,
         * as discussed in the Champ class. Also reads in a leading integer corresponding to the total number of games
         * from which data such as picks and bans are coming.
         */
        Champ[] temp_array = new Champ[ChampList.champ_names.length];
        Champ.PopulateChampIDs();
        String buffer;
        int count = 0;
        buffer = champ_buffered_old.readLine();
        if(!(buffer == null)){
            window.data.total_games = Integer.parseInt(buffer);
        }
        String[] temp_string_arr;
        int win, loss, kill, death, ass;
        double csd10;
        buffer = champ_buffered_old.readLine();
        while(buffer != null){
            String name = buffer;
            String key = Champ.GetID(MakeSafe(name));
            Champ temp = new Champ(name, key);
            buffer = champ_buffered_old.readLine();
            temp_string_arr = buffer.split("-");
            win = Integer.parseInt(temp_string_arr[0]);
            loss = Integer.parseInt(temp_string_arr[1]);
            buffer = champ_buffered_old.readLine();
            temp_string_arr = buffer.split("-");
            kill = Integer.parseInt(temp_string_arr[0]);
            death = Integer.parseInt(temp_string_arr[1]);
            ass = Integer.parseInt(temp_string_arr[2]);
            buffer = champ_buffered_old.readLine();
            csd10 = Double.parseDouble(buffer);
            temp.SetData(win, loss, kill, death, ass, csd10);
            temp_array[count] = temp;
            count ++;
            buffer = champ_buffered_old.readLine();
        }
        window.data.champ_array = temp_array;
        champ_buffered_old.close();
    }

    private String MakeSafe(String start){
        /* Edits the names of champions to the formats returned by DataDragon's JSON list. If name does not need edits,
         * it will not appear here.
         */
        if(start.equals("Wukong")){
            return "MonkeyKing";
        }
        else if(start.equals("Aurelion Sol")){
            return "AurelionSol";
        }
        else if(start.equals("Cho'Gath")){
            return "Chogath";
        }
        else if(start.equals("Dr. Mundo")){
            return "DrMundo";
        }
        else if(start.equals("Jarvan IV")){
            return "JarvanIV";
        }
        else if(start.equals("Kai'Sa")){
            return "Kaisa";
        }
        else if(start.equals("Kha'zix")){
            return "Khazix";
        }
        else if(start.equals("Kog'Maw")){
            return "KogMaw";
        }
        else if(start.equals("LeBlanc")){
            return "Leblanc";
        }
        else if(start.equals("Lee Sin")){
            return "LeeSin";
        }
        else if(start.equals("Master Yi")){
            return "MasterYi";
        }
        else if(start.equals("Miss Fortune")){
            return "MissFortune";
        }
        else if(start.equals("Rek'Sai")){
            return "RekSai";
        }
        else if(start.equals("Tahm Kench")){
            return "TahmKench";
        }
        else if(start.equals("Twisted Fate")){
            return "TwistedFate";
        }
        else if(start.equals("Vel'Koz")){
            return "Velkoz";
        }
        else if(start.equals("Xin Zhao")){
            return "XinZhao";
        }
        else {
            return start;
        }
    }

    public void WritePlayerData(String abbr, Player current){
        File directory = new File(".\\data\\" + abbr);
        if(!directory.exists()){
            directory.mkdirs();
        }
        String[] array = current.Stringify();
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(".\\data\\" + abbr + "\\" + current.handle + ".txt"));
            for(int i = 0; i < array.length; i++){
                output.write(array[i]);
                output.write("\n");
            }
            output.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ReadPlayerData(String abbr, Player current){
        int w = 0, l = 0, k = 0, d = 0, a = 0;
        double csd = 0, csm = 0, vis = 0, g = 0, neut = 0, neut_en = 0;
        String buffer;
        String[] array;
        try{
            BufferedReader input = new BufferedReader(new FileReader(".\\data\\" + abbr + "\\" + current.handle + ".txt"));
            buffer = input.readLine();
            array = buffer.split("-");
            w = Integer.parseInt(array[0]);
            l = Integer.parseInt(array[1]);
            buffer = input.readLine();
            array = buffer.split("-");
            k = Integer.parseInt(array[0]);
            d = Integer.parseInt(array[1]);
            a = Integer.parseInt(array[2]);
            buffer = input.readLine();
            array = buffer.split("~");
            csd = Double.parseDouble(array[0]);
            csm = Double.parseDouble(array[1]);
            buffer = input.readLine();
            array = buffer.split("~");
            neut = Double.parseDouble(array[0]);
            neut_en = Double.parseDouble(array[1]);
            buffer = input.readLine();
            array = buffer.split("~");
            g = Double.parseDouble(array[0]);
            vis = Double.parseDouble(array[1]);
            input.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        current.SetStats(w, l, k, d, a, csd, csm, vis, neut, neut_en, g);
    }
}
//Test