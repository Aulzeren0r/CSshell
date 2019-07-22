public class PlayerDataBox {
    boolean w;
    int k;
    int d;
    int a;
    double csd;
    double csm;
    int v;
    int n;
    int en_neut;
    int g;

    public PlayerDataBox(boolean win, int kills, int deaths, int assists, double csdiff, double csmin, int vision,
                         int neutrals, int enemy_neutrals, int gold){
        w = win;
        k = kills;
        d = deaths;
        a = assists;
        csd = csdiff;
        csm = csmin;
        v = vision;
        n = neutrals;
        en_neut = enemy_neutrals;
        g = gold;
    }
}
