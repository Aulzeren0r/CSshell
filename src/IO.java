import javax.swing.*;
import java.io.*;
import java.nio.Buffer;

public class IO {
    TopLevelRewrite window;
    BufferedReader team_buffered_old;
    BufferedWriter team_buffered;
    BufferedReader champ_buffered_old;
    BufferedWriter champ_buffered;

    int new_flag;
    static final int TEAM = 10;
    static final int CHAMP = 20;

    public IO(TopLevelRewrite main_window) throws IOException{
        try {
            team_buffered_old = new BufferedReader(new FileReader(".\\data\\team_data.dat"));
            team_buffered = new BufferedWriter(new FileWriter(".\\data\\team_data_temp.dat"));
            champ_buffered_old = new BufferedReader(new FileReader(".\\data\\champ_data.dat"));
            champ_buffered = new BufferedWriter(new FileWriter(".\\data\\champ_data_temp.dat"));
        }
        catch(IOException e){
            team_buffered = new BufferedWriter(new FileWriter(".\\data\\team_data.dat"));
            champ_buffered = new BufferedWriter(new FileWriter(".\\data\\champ_data.dat"));
        }
        window = main_window;
    }

    public void PushStrings(String[] strings, int type_flag) throws IOException{
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
        try {
            team_buffered.close();
            champ_buffered.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ReplaceOldFiles();
    }

    private void ReplaceOldFiles(){
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
            window.data.AddTeam(temp_team);
            i = 0;
            array = new String[40];
            buffer = team_buffered_old.readLine();
        }
        team_buffered_old.close();


    }

    public void ReadChampData() throws IOException{
        String buffer;
        buffer = champ_buffered_old.readLine();
        int[] array_buffer = new int[5];
        for(int i = 0; i < 5; i++){
            array_buffer[i] = 0;
        }
        BufferedWriter datadump = new BufferedWriter(new FileWriter(".\\data\\datadump.dat"));
        Champ[] temp_array = new Champ[ChampList.champ_names.length];
        int count = 0;
        while(buffer != null && count < ChampList.champ_names.length){
            Champ temp = new Champ("", array_buffer, array_buffer,"");
            temp.name = buffer;
            buffer = champ_buffered_old.readLine();
            String[] temp_string = buffer.split(" ");
            for(int i = 0; i < 5; i++){
                array_buffer[i] = Integer.parseInt(temp_string[i]);
            }
            System.arraycopy(array_buffer, 0, temp.wins, 0, 5);
            buffer = champ_buffered_old.readLine();
            temp_string = buffer.split(" ");
            for(int i = 0; i < 5; i++){
                array_buffer[i] = Integer.parseInt(temp_string[i]);
            }
            System.arraycopy(array_buffer, 0, temp.losses, 0, 5);
            buffer = MakeSafe(temp.name);
            temp.loc = ".\\data\\img\\" + buffer + ".jpg";
            temp_array[count] = temp;
            count++;
            buffer = champ_buffered_old.readLine();
        }
        window.data.champ_array = new Champ[count];
        System.arraycopy(temp_array, 0, window.data.champ_array, 0, count);
        champ_buffered_old.close();
        for(int i = 0; i < 3; i++){
            String[] temp2 = window.data.champ_array[2].Stringify();
            for(int j = 0; j < 3; j++){
                datadump.write(temp2[j]);
                datadump.write("\n");
            }
        }
        datadump.close();
    }

    private String MakeSafe(String start){
        start = start.replace(" ", "_");
        start = start.replace("'", "_");
        start = start.replace(".", "");
        start = start.toLowerCase();
        return start;
    }
}
