import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class RiotAPICall {
    /* Helper class dedicated to directly polling either api.riotgames.com or DDragon, depending on the context.
     * This class does minimal parsing of data, leaving all nonessential parsing for its purposes to APIData instead.
     */
    static final String url_lead = "https://na1.api.riotgames.com/lol/match/v4/matches/";
    static String api_key;
    static String curr_version;
    static String cdn_lead;

    public static void SetKey(String new_key){
        //Sets the key for accessing API data from the text document provided in /data. Will be static once project is complete and approved.
        api_key = new_key;
    }

    public static JSONObject GetRawAPI(String match_id){
        //Pulls API data for a given match. Will be the gateway to automatic stat handling once upgrade is complete.
        String url = url_lead + match_id + "?api_key=" + api_key;
        JSONObject temp = GenAPICall(url);
        return temp;
    }

    public static void GetCurrentVersion(){
        //Pulls DDragon config info to get current DDragon version and ensure correct static data URI lead.
        String url = "https://ddragon.leagueoflegends.com/realms/na.json";
        JSONObject realms = GenAPICall(url);
        curr_version = realms.getJSONObject("n").getString("champion");
        cdn_lead = realms.getString("cdn");

    }

    public static JSONObject GetChampsList(){
        //Pulls list of champions currently in-game in order to extract champion ID data.
        String url = cdn_lead + "/" + curr_version + "/data/en_US/champion.json";
        JSONObject champ_list = GenAPICall(url);
        JSONObject data = champ_list.getJSONObject("data");
        return data;
    }

    private static JSONObject GenAPICall(String Url){
        //Generalized API call. Takes a given URI and returns the top-layer JSON object from that URI.
        StringBuilder fulltext = new StringBuilder();
        BufferedWriter game_raw = null;
        if(Url.contains("/v4/matches")){
            String[] temp = Url.split("/");
            temp = temp[7].split("\\?");
            try {
                game_raw = new BufferedWriter(new FileWriter(".\\data\\games\\" + temp[0] + ".txt"));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        try {
            URL site = new URL(Url);
            HttpURLConnection con = (HttpURLConnection) site.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if(responseCode == 429){
                JOptionPane.showMessageDialog(null, "Error, API call limit overrun. THIS SHOULD NEVER HAPPEN.");
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String buffer;
            while((buffer = in.readLine()) != null){
                fulltext.append(buffer);
                if(game_raw != null){
                    game_raw.write(buffer);
                }
            }
            in.close();
            if(game_raw != null){
                game_raw.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        JSONObject temp = new JSONObject(fulltext.toString());
        return temp;
    }

    public static String PullSummonerID(String player_name){
        String lead = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/";
        String url = lead + player_name + "?api_key=" + api_key;
        JSONObject output = GenAPICall(url);
        return output.getString("accountId");
    }

    public static void UpdateAPIKey(String new_key){
        try {
            BufferedWriter api_txt = new BufferedWriter(new FileWriter(".\\data\\static\\api_key.txt"));
            api_txt.write(new_key);
            api_txt.write("\n");
            api_txt.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        api_key = new_key;
    }

    public static JSONObject PullMatchData(String encrypt_id, String champ_key, long epoch_start){
        String url = "https://na1.api.riotgames.com/lol/match/v4/matchlists/by-account/";
        String link = url + encrypt_id + "?api_key=" + api_key + "&champion=" + champ_key + "&beginTime=" + epoch_start
                + "&endIndex=1";
        JSONObject matchlist = GenAPICall(link);
        JSONArray matches = matchlist.getJSONArray("matches");
        JSONObject match_header = matches.getJSONObject(0);
        return match_header;
    }
}
