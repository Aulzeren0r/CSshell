import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        try {
            URL site = new URL(Url);
            HttpURLConnection con = (HttpURLConnection) site.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if(responseCode == 403){
                JOptionPane.showMessageDialog(null, "Error, API call limit overrun. THIS SHOULD NEVER HAPPEN.");
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String buffer;
            while((buffer = in.readLine()) != null){
                fulltext.append(buffer);
            }
            in.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        JSONObject temp = new JSONObject(fulltext.toString());
        return temp;
    }
}
