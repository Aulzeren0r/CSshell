import java.lang.Math;
public class Player {
    //Player data-storage class. Currently WIP pending Riot API upgrade.
   String first_name;
   String handle;
   String last_name;
   String headshot_loc;
   int roles;
   int active_role;

   //TODO Player Stats
   /* Kills
    * Deaths
    * Assists
    * CS @ 10
    * CS/min
    * Vision Score
    * Neutral Minions
    * Neutral Minions - Enemy
    * Damage Taken
    * Damage Dealt
    * Wards Placed
    * CC Score
    * Gold Earned
    *
    */

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

   public Player(String first, String hand, String last, int role_value, int active_role, String team_abbr){
       //Player declaration. Will be updated/abridged/adjusted pending Riot API update.
       setFirstName(first);
       setHandle(hand);
       setLastName(last);
       setRoles(role_value);
       setActiveRole(active_role);
       headshot_loc = "/" + team_abbr + "/" + hand + ".jpg";
   }


}
