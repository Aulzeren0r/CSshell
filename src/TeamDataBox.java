public class TeamDataBox {
    boolean w;
    int k;
    int d;
    int a;
    int g;
    int i;
    int t;
    int b;
    int dr;
    int h;
    boolean fd;
    int eb;
    int ed;
    int eh;

    public TeamDataBox(boolean win, int kills, int deaths, int assists, int gold, int inhibs, int towers, int baron,
                       int drag, int heralds, boolean first_drag, int enemy_baron, int enemy_drake, int enemy_herald){
        w = win;
        k = kills;
        d = deaths;
        a = assists;
        g = gold;
        i = inhibs;
        t = towers;
        b = baron;
        dr = drag;
        h = heralds;
        fd = first_drag;
        eb = enemy_baron;
        ed = enemy_drake;
        eh = enemy_herald;
    }
}
