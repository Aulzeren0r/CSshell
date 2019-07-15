import java.lang.Math;
public class Player {
    //Player data-storage class. Currently WIP pending Riot API upgrade.
   String first_name;
   String handle;
   String last_name;
   String headshot_loc;
   int roles;
   int active_role;
   int win;
   int loss;
   int kills;
   int deaths;
   int assists;
   double CSD10;
   double CSmin;
   double vis;
   double neutrals;
   double neutral_enemy;
   double gold;


   //Fairly self-explanatory outward-facing assignment functions.
   public void setActiveRole(int role_selected){
       active_role = role_selected;
   }

   public void setRoles(int new_roles){
       roles = new_roles;
   }

   public void setLastName(String new_name){
       last_name = new_name;
   }

   public void setHandle(String new_handle){
       handle = new_handle;
   }

   public void setFirstName(String new_name){
       first_name = new_name;
   }

   public void SetKills(int new_k){
       kills = new_k;
   }

   public void SetDeaths(int new_d){
       deaths = new_d;
   }

   public void SetAssists(int new_a){
       assists = new_a;
   }

   public void SetWins(int wins){
       win = wins;
   }

   public void SetLoss(int losses){
       loss = losses;
   }

   public void SetCSD(double CSD){
       CSD10 = CSD;
   }

   public void SetCSM(double CSM){
       CSmin = CSM;
   }

   public void SetVision(double score){
       vis = score;
   }

   public void SetNeutrals(double neutral){
       neutrals = neutral;
   }

   public void SetEnemyNeuts(double neutral){
       neutral_enemy = neutral;
   }

   public void SetGold(double gpg){
       gold = gpg;
   }

   public Player(String first, String hand, String last, int role_value, int active_role, String team_abbr){
       //Player declaration. Will be updated/abridged/adjusted pending Riot API update.
       setFirstName(first);
       setHandle(hand);
       setLastName(last);
       setRoles(role_value);
       setActiveRole(active_role);
       headshot_loc = "/" + team_abbr + "/" + hand + ".jpg";
       InitStats();
   }

   public void SetStats(int w, int l, int k, int d, int a, double csd, double csm, double v, double neut,
                        double neut_en, double g){
       SetWins(w);
       SetLoss(l);
       SetKills(k);
       SetDeaths(d);
       SetAssists(a);
       SetCSD(csd);
       SetCSM(csm);
       SetVision(v);
       SetNeutrals(neut);
       SetEnemyNeuts(neut_en);
       SetGold(g);
   }

   public void NewStats(boolean w, int k, int d, int a, int csd, double csm, int v, int n, int en_neut, int g){
       double games = win + loss;
       double temp = vis *  games;
       temp += v;
       SetVision(temp /  (games + 1));
       temp = neutrals *  games;
       temp += n;
       SetNeutrals( temp /  (games + 1));
       temp = neutral_enemy *  games;
       temp += en_neut;
       SetEnemyNeuts(temp /  (games + 1));
       temp = gold *  games;
       temp += g;
       SetGold(temp /  (games + 1));
       temp = CSmin * games;
       temp += csm;
       SetCSM(temp / (games + 1));
       temp = CSD10 * games;
       temp += csd;
       SetCSD(temp / (games + 1));
       if(w){
           SetWins(win + 1);
       }
       else{
           SetLoss(loss + 1);
       }
       SetKills(kills + k);
       SetDeaths(deaths + d);
       SetAssists(assists + a);
   }

    private void InitStats(){
       kills = 0;
       deaths = 0;
       win = 0;
       loss = 0;
       assists = 0;
       CSD10 = 0;
       CSmin = 0;
       vis = 0;
       neutrals = 0;
       neutral_enemy = 0;
       gold = 0;
    }

    public String[] Stringify(){
       String[] temp = new String[5];
       temp[0] = win + "-" + loss;
       temp[1] = kills + "-" + deaths + "-" + assists;
       temp[2] = CSD10 + "~" + CSmin;
       temp[3] = neutrals + "~" + neutral_enemy;
       temp[4] = gold + "~" + vis;
       return temp;
    }

    public double GetKDA(){
       double ka = kills + assists;
       double kda = ka / deaths;
       if(Double.isNaN(kda)){
           return -1;
       }
       return kda;
    }
}
