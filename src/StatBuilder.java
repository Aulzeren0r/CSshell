public class StatBuilder {
    DisplayWindow dw;
    int type_flag;
    int flag_1;
    int flag_2;
    int flag_3;

    public StatBuilder(DisplayWindow dwind){
        type_flag = 0;
        flag_1 = 0;
        flag_2 = 0;
        flag_3 = 0;
        dw = dwind;
    }

    public boolean SetFlag(int sub_flag, int value){
        if(sub_flag == 0){
           type_flag = value;
        }
        else if(sub_flag == 1){
            if(flag_1 == 0){
                flag_1 = value;
            }
            else if(flag_2 == 0){
                flag_2 = value;
            }
            else if(flag_3 == 0){
                flag_3 = value;
            }
            else{
                return false;
            }
        }
        return true;
    }
}
