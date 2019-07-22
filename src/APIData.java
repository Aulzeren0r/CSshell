import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;

public class APIData {
    /* WIP.
     * Holder class for most Riot API post-match data, as well as processor of said data. Currently under construction,
     * as the data structure of this program is being retooled.
     */
    TopLevelRewrite tlr;

    public void PostGameHandler(){
        try{
            File games_folder = new File(".\\data\\games");
            if(!games_folder.exists()){
                games_folder.mkdirs();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        long epoch_current = System.currentTimeMillis();

        long epoch_pre_game = epoch_current - (518400 * 1000);
        Player target = null;
        for(Player player : tlr.data.blue_team.roster){
            if(player.active_role == 1){
                target = player;
                break;
            }
        }
        if(target == null){
            JOptionPane.showMessageDialog(tlr.main_frame, "Error, target player could not be locked.");
            return;
        }
        String encrypt_id = RiotAPICall.PullSummonerID(SafeString(target.handle));
        String champ_id = tlr.data.FindChamp(tlr.data.blue_champs[0]).champ_id;
        JSONObject match_data = RiotAPICall.PullMatchData(encrypt_id, champ_id, epoch_pre_game);
        long game_id = match_data.getLong("gameId");
        JSONObject game_data = RiotAPICall.GetRawAPI(Long.toString(game_id));
        int game_time = game_data.getInt("gameDuration");
        double game_minutes = (double) game_time / 60.0;
        JSONArray participant_array = game_data.getJSONArray("participantIdentities");
        int[] blue_ids = new int[5];
        Player[] blue_players = new Player[5];
        int[] red_ids = new int[5];
        Player[] red_players = new Player[5];
        for(Player p : tlr.data.blue_team.roster){
            switch(p.active_role){
                case 1:
                    blue_players[0] = p;
                    break;
                case 2:
                    blue_players[1] = p;
                    break;
                case 4:
                    blue_players[2] = p;
                    break;
                case 8:
                    blue_players[3] = p;
                    break;
                case 16:
                    blue_players[4] = p;
                    break;
            }
        }
        for(Player p : tlr.data.red_team.roster){
            switch(p.active_role){
                case 1:
                    red_players[0] = p;
                    break;
                case 2:
                    red_players[1] = p;
                    break;
                case 4:
                    red_players[2] = p;
                    break;
                case 8:
                    red_players[3] = p;
                    break;
                case 16:
                    red_players[4] = p;
                    break;
            }
        }
        for(Object o : participant_array){
            JSONObject participant_data = (JSONObject) o;
            int id = participant_data.getInt("participantId");
            String handle = participant_data.getJSONObject("player").getString("summonerName");
            handle = handle.trim();
            for(int i = 0; i < 5; i++){
                Player player = blue_players[i];
                if(handle.equals(player.handle)){
                    blue_ids[i] = id;
                    break;
                }
                player = red_players[i];
                if(handle.equals(player.handle)){
                    red_ids[i] = id;
                    break;
                }
            }
        }

        participant_array = game_data.getJSONArray("teams");
        TeamDataBox[] team_data = new TeamDataBox[2];
        for(int i = 0; i < 2; i++){
            team_data[i] = new TeamDataBox(false, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, false, 0, 0, 0);
        }
        for(Object o : participant_array){
            JSONObject team = (JSONObject) o;
            int red_blue = -1;
            switch(team.getInt("teamId")){
                case 100:
                    red_blue = 0;
                    break;
                case 200:
                    red_blue = 1;
                    break;
            }
            if(red_blue == -1){
                JOptionPane.showMessageDialog(null, "FUCKING SHIT'S BUSTED");
                tlr.PopulateLander();
                return;
            }
            JSONArray bans = team.getJSONArray("bans");
            for(int i = 0; i < 5; i++){
                if(bans.getJSONObject(i).getInt("championId") == -1){
                    continue;
                }
                Champ target_champ = tlr.data.FindByKey(Integer.toString(bans.getJSONObject(i).getInt("championId")));
                target_champ.InsertData(false, false, true, 0, 0, 0, 0);
            }
            team_data[red_blue].fd = team.getBoolean("firstDragon");
            if(team.getString("win").equals("Win")){
                team_data[red_blue].w = true;
            }
            team_data[red_blue].t = team.getInt("towerKills");
            team_data[red_blue].i = team.getInt("inhibitorKills");
            team_data[red_blue].h = team.getInt("riftHeraldKills");
            team_data[red_blue].dr = team.getInt("dragonKills");
            team_data[red_blue].b = team.getInt("baronKills");
        }

        team_data[0].eb = team_data[1].b;
        team_data[0].eh = team_data[1].h;
        team_data[0].ed = team_data[1].dr;
        team_data[1].eb = team_data[0].b;
        team_data[1].eh = team_data[0].h;
        team_data[1].ed = team_data[0].dr;

        double[] cs_10 = new double[10];
        PlayerDataBox[] player_data = new PlayerDataBox[10];
        for(int i = 0; i < 10; i++){
            player_data[i] = new PlayerDataBox(false, 0, 0, 0, 0, 0, 0,
                    0, 0, 0);
        }

        int player_no = 0;
        participant_array = game_data.getJSONArray("participants");
        for(Object o : participant_array){
            JSONObject player_json = (JSONObject) o;
            int part_id = player_json.getInt("participantId");
            for(int i = 0; i < 5; i++){
                if(part_id == blue_ids[i]){
                    player_no = i;
                    player_data[player_no].w = team_data[0].w;
                    break;
                }
                if(part_id == red_ids[i]){
                    player_no = i + 5;
                    player_data[player_no].w = team_data[1].w;
                    break;
                }
            }
            JSONObject player_stats = player_json.getJSONObject("stats");
            player_data[player_no].k = player_stats.getInt("kills");
            player_data[player_no].d = player_stats.getInt("deaths");
            player_data[player_no].a = player_stats.getInt("assists");
            player_data[player_no].g = player_stats.getInt("goldEarned");
            player_data[player_no].n = player_stats.getInt("neutralMinionsKilled");
            player_data[player_no].en_neut = player_stats.getInt("neutralMinionsKilledEnemyJungle");
            player_data[player_no].v = player_stats.getInt("visionScore");
            player_data[player_no].csm = (double) player_stats.getInt("totalMinionsKilled") / game_minutes;
            cs_10[player_no] = player_json.getJSONObject("timeline").getJSONObject("creepsPerMinDeltas").getDouble("0-10") * 10;
        }

        for(int i = 0; i < 5; i++){
            double cs_diff = cs_10[i] - cs_10[i + 5];
            player_data[i].csd = cs_diff;
            player_data[i + 5].csd = 0 - cs_diff;
        }

        for(int i = 0; i < 5; i++){
            team_data[0].k += player_data[i].k;
            team_data[0].d += player_data[i].d;
            team_data[0].a += player_data[i].a;
            team_data[0].g += player_data[i].g;

            team_data[1].k += player_data[i + 5].k;
            team_data[1].d += player_data[i + 5].d;
            team_data[1].a += player_data[i + 5].a;
            team_data[1].g += player_data[i + 5].g;

            blue_players[i].NewStats(player_data[i]);
            red_players[i].NewStats(player_data[i + 5]);

            Champ target_champ = tlr.data.FindChamp(tlr.data.blue_champs[i]);
            target_champ.InsertData(player_data[i].w, !player_data[i].w, false, player_data[i].k,
                    player_data[i].d, player_data[i].a, player_data[i].csd);
            target_champ = tlr.data.FindChamp(tlr.data.red_champs[i]);
            target_champ.InsertData(player_data[i + 5].w, !player_data[i + 5].w, false, player_data[i + 5].k,
                    player_data[i + 5].d, player_data[i + 5].a, player_data[i + 5].csd);
        }

        tlr.data.red_team.StatUpdate(team_data[1]);
        tlr.data.blue_team.StatUpdate(team_data[0]);

        DataHandler.total_games += 1;
        tlr.PopulateLander();
        JOptionPane.showMessageDialog(tlr.main_frame, "Data load complete. For raw JSON of game, check: \".\\data\\games\\" + game_id + ".txt.");
    }

    public APIData(TopLevelRewrite tlr_pass){
        tlr = tlr_pass;
    }

    private void ChampBan(String key){
        Champ target = tlr.data.FindByKey(key);
        target.InsertData(false, false, true, 0, 0, 0, 0);
    }

    private void ChampUpdate(String key, boolean win, int k, int d, int a, double csd){
        Champ target = tlr.data.FindByKey(key);
        target.InsertData(win, !win, false, k, d, a, csd);
    }

    private String SafeString(String handle){
        StringBuilder output = new StringBuilder(25);
        String[] temp = handle.split(" ");
        for(int i = 0; i < temp.length; i++){
            output.append(temp[i]);
            output.append("%20");
        }
        String out_string = output.toString();
        return out_string;
    }

}
