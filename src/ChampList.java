import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ChampList {
    /* A static class composed of an array of current champions in LoL. Main2() is an alternative function to be run
     * if the directory does not yet contain a champ_data.txt, and will initialize it with all data equal to 0.
     */
    public static final String[] champ_names = {
        "Aatrox",
                "Ahri",
                "Akali",
                "Alistar",
                "Amumu",
                "Anivia",
                "Annie",
                "Ashe",
                "Aurelion Sol",
                "Azir",
                "Bard",
                "Blitzcrank",
                "Brand",
                "Braum",
                "Caitlyn",
                "Camille",
                "Cassiopeia",
                "Cho'Gath",
                "Corki",
                "Darius",
                "Diana",
                "Dr. Mundo",
                "Draven",
                "Ekko",
                "Elise",
                "Evelynn",
                "Ezreal",
                "Fiddlesticks",
                "Fiora",
                "Fizz",
                "Galio",
                "Gangplank",
                "Garen",
                "Gnar",
                "Gragas",
                "Graves",
                "Hecarim",
                "Heimerdinger",
                "Illaoi",
                "Irelia",
                "Ivern",
                "Janna",
                "Jarvan IV",
                "Jax",
                "Jayce",
                "Jhin",
                "Jinx",
                "Kai'Sa",
                "Kalista",
                "Karma",
                "Karthus",
                "Kassadin",
                "Katarina",
                "Kayle",
                "Kayn",
                "Kennen",
                "Kha'zix",
                "Kindred",
                "Kled",
                "Kog'Maw",
                "LeBlanc",
                "Lee Sin",
                "Leona",
                "Lissandra",
                "Lucian",
                "Lulu",
                "Lux",
                "Malphite",
                "Malzahar",
                "Maokai",
                "Master Yi",
                "Miss Fortune",
                "Mordekaiser",
                "Morgana",
                "Nami",
                "Nasus",
                "Nautilus",
                "Neeko",
                "Nidalee",
                "Nocturne",
                "Nunu",
                "Olaf",
                "Orianna",
                "Ornn",
                "Pantheon",
                "Poppy",
                "Pyke",
                "Qiyana",
                "Quinn",
                "Rakan",
                "Rammus",
                "Rek'Sai",
                "Renekton",
                "Rengar",
                "Riven",
                "Rumble",
                "Ryze",
                "Sejuani",
                "Shaco",
                "Shen",
                "Shyvana",
                "Singed",
                "Sion",
                "Sivir",
                "Skarner",
                "Sona",
                "Soraka",
                "Swain",
                "Sylas",
                "Syndra",
                "Tahm Kench",
                "Taliyah",
                "Talon",
                "Taric",
                "Teemo",
                "Thresh",
                "Tristana",
                "Trundle",
                "Tryndamere",
                "Twisted Fate",
                "Twitch",
                "Udyr",
                "Urgot",
                "Varus",
                "Vayne",
                "Veigar",
                "Vel'Koz",
                "Vi",
                "Viktor",
                "Vladimir",
                "Volibear",
                "Warwick",
                "Wukong",
                "Xayah",
                "Xerath",
                "Xin Zhao",
                "Yasuo",
                "Yorick",
                "Yuumi",
                "Zac",
                "Zed",
                "Ziggs",
                "Zilean",
                "Zoe",
                "Zyra"
    };

    public static void main2(String[] args) throws IOException {
        //Courtesy function for testing. Builds champ_data.txt, or resets to 0 if already exists.
        BufferedWriter temp = new BufferedWriter(new FileWriter(".\\data\\champ_data.dat"));
        for(int i = 0; i < champ_names.length; i++){
            temp.write(champ_names[i]);
            temp.write("\n");
            temp.write("0-0");
            temp.write("\n");
            temp.write("0-0-0");
            temp.write("\n");
            temp.write("0.0");
            temp.write("\n");
        }
        temp.close();
    }

}
