import java.lang.Math;
public class Player {
   String first_name;
   String handle;
   String last_name;
   String headshot_loc;
   int roles;
   int active_role;

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
       setFirstName(first);
       setHandle(hand);
       setLastName(last);
       setRoles(role_value);
       setActiveRole(active_role);
       headshot_loc = "/" + team_abbr + "/" + hand + ".jpg";
   }


}
