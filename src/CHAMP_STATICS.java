/*
 * Copyright (c) Chris Attias, $month.year. Use of this code is reserved solely for the University of Alabama League of Legends Club.
 */

import java.util.HashMap;

public class CHAMP_STATICS {
    /* CURRENTLY UNDER RENOVATION.
     * A static class used to hold the crop details of the champion portraits pulled from DDragon for champion
     * select. When possible, the full image will be used instead, but this is not an option due to how DisplayWindow
     * is built.
     */
    static private final HashMap<String, int[]> champ_map = new HashMap<String, int[]>(){
        {
            put("Aatrox", new int[] {58, 99, 250, 126, 0});
            put("Ahri", new int[] {0, 59, 250, 126, 1});
            put("Akali", new int[] {58, 23, 250, 126, 1});
            put("Alistar", new int[] {3, 165, 305, 170, 1});
            put("Amumu", new int[] {14, 62, 294, 209, 1});
            put("Anivia", new int[] {27, 101, 258, 184, 1});
            put("Annie", new int[] {18, 65, 271, 175, 1});
            put("Ashe", new int[] {4, 60, 238, 125, 1});
            put("Aurelion Sol", new int[] {4, 16, 304, 195, 0});
            put("Azir", new int[] {62, 39, 219, 118, 1});
            put("Bard", new int[] {59, 80, 248, 116, 0});
            put("Blitzcrank", new int[] {75, 128, 233, 106, 1});
            put("Brand", new int[] {93, 62, 200, 120, 0});
            put("Braum", new int[] {9, 55, 242, 110, 1});
            put("Caitlyn", new int[] {12, 68, 250, 108, 1});
            put("Camille", new int[] {122, 43, 186, 115, 0});
            put("Cassiopeia", new int[] {0, 127, 213, 116, 1});
            put("Cho'Gath", new int[] {2, 84, 306, 267, 1});
            put("Corki", new int[] {28, 66, 240, 129, 0});
            put("Darius", new int[] {4, 1, 238, 156, 1});
            put("Diana", new int[] {18, 48, 227, 151, 1});
            put("Dr. Mundo", new int[] {81, 69, 227, 164, 0});
            put("Draven", new int[] {16, 51, 199, 143, 1});
            put("Ekko", new int[] {88, 85, 210, 150, 0});
            put("Elise", new int[] {70, 71, 160, 95, 0});
            put("Evelynn", new int[] {72, 48, 203, 117, 0});
            put("Ezreal", new int[] {13, 74, 222, 138, 1});
            put("Fiddlesticks", new int[] {16, 66, 274, 200, 1});
            put("Fiora", new int[] {21, 27, 217, 124, 1});
            put("Fizz", new int[] {21, 57, 236, 143, 1});
            put("Galio", new int[] {114, 11, 190, 95, 0});
            put("Gangplank", new int[] {18, 47, 254, 188, 1});
            put("Garen", new int[] {79, 114, 229, 171, 1});
            put("Gnar", new int[] {10, 168, 298, 227, 0});
            put("Gragas", new int[] {49, 79, 182, 118, 1});
            put("Graves", new int[] {29, 21, 226, 116, 1});
            put("Hecarim", new int[] {103, 61, 164, 90, 1});
            put("Heimerdinger", new int[] {3, 87, 305, 270, 1});
            put("Illaoi", new int[] {43, 18, 162, 97, 0});
            put("Irelia", new int[] {95, 23, 167, 89, 0});
            put("Ivern", new int[] {43, 48, 265, 192, 0});
            put("Janna", new int[] {76, 89, 218, 98, 1});
            put("Jarvan IV", new int[] {26, 77, 207, 110, 1});
            put("Jax", new int[] {130, 56, 178, 120, 0});
            put("Jayce", new int[] {88, 61, 166, 112, 1});
            put("Jhin", new int[] {6, 38, 253, 147, 0});
            put("Jinx", new int[] {44, 45, 209, 124, 0});
            put("Kai'Sa", new int[] {20, 32, 235, 130, 1});
            put("Kalista", new int[] {66, 77, 186, 112, 1});
            put("Karma", new int[] {56, 52, 185, 136, 1});
            put("Karthus", new int[] {34, 37, 257, 130, 1});
            put("Kassadin", new int[] {1, 73, 307, 247, 1});
            put("Katarina", new int[] {7, 95, 277, 179, 1});
            put("Kayle", new int[] {110, 25, 197, 125, 0});
            put("Kayn", new int[] {60, 38, 248, 149, 0});
            put("Kennen", new int[] {32, 86, 276, 162, 0});
            put("Kha'zix", new int[] {56, 159, 252, 142, 0});
            put("Kindred", new int[] {28, 151, 178, 106, 0});
            put("Kled", new int[] {13, 45, 295, 182, 0});
            put("Kog'Maw", new int[] {1, 220, 307, 230, 1});
            put("LeBlanc", new int[] {56, 63, 222, 120, 0});
            put("Lee Sin", new int[] {33, 83, 196, 82, 1});
            put("Leona", new int[] {26, 28, 192, 104, 1});
            put("Lissandra", new int[] {56, 73, 165, 90, 1});
            put("Lucian", new int[] {57, 61, 224, 122, 1});
            put("Lulu", new int[] {29, 151, 279, 157, 1});
            put("Lux", new int[] {20, 82, 288, 136, 1});
            put("Malphite", new int[] {36, 261, 272, 142, 1});
            put("Malzahar", new int[] {47, 40, 261, 132, 0});
            put("Maokai", new int[] {60, 178, 248, 133, 1});
            put("Master Yi", new int[] {53, 122, 209, 123, 1});
            put("Miss Fortune", new int[] {39, 58, 238, 120, 1});
            put("Mordekaiser", new int[] {71, 20, 189, 125, 0});
            put("Morgana", new int[] {67, 46, 183, 97, 1});
            put("Nami", new int[] {63, 39, 228, 129, 1});
            put("Nasus", new int[] {32, 107, 177, 99, 1});
            put("Nautilus", new int[] {4, 51, 304, 193, 1});
            put("Neeko", new int[] {31, 70, 277, 140, 1});
            put("Nidalee", new int[] {107, 80, 201, 99, 1});
            put("Nocturne", new int[] {38, 168, 270, 153, 1});
            put("Nunu", new int[] {65, 256, 164, 74, 1});
            put("Olaf", new int[] {33, 71, 272, 136, 1});
            put("Orianna", new int[] {79, 43, 229, 125, 1});
            put("Ornn", new int[] {24, 60, 248, 207, 0});
            put("Pantheon", new int[] {65, 47, 228, 145, 0});
            put("Poppy", new int[] {57, 76, 251, 136, 0});
            put("Pyke", new int[] {12, 102, 232, 162, 1});
            put("Qiyana", new int[] {9, 65, 233, 125, 1});
            put("Quinn", new int[] {71, 127, 181, 100, 1});
            put("Rakan", new int[] {10, 20, 221, 130, 1});
            put("Rammus", new int[] {6, 170, 302, 148, 1});
            put("Rek'Sai", new int[] {1, 122, 307, 200, 1});
            put("Renekton", new int[] {2, 48, 306, 221, 1});
            put("Rengar", new int[] {36, 151, 268, 144, 1});
            put("Riven", new int[] {33, 37, 240, 146, 1});
            put("Rumble", new int[] {7, 72, 275, 154, 1});
            put("Ryze", new int[] {69, 75, 239, 144, 0});
            put("Sejuani", new int[] {68, 36, 240, 100, 0});
            put("Shaco", new int[] {2, 65, 221, 109, 1});
            put("Shen", new int[] {60, 21, 226, 118, 1});
            put("Shyvana", new int[] {49, 73, 216, 93, 0});
            put("Singed", new int[] {53, 72, 255, 127, 0});
            put("Sion", new int[] {30, 47, 182, 91, 1});
            put("Sivir", new int[] {9, 148, 242, 131, 1});
            put("Skarner", new int[] {3, 166, 305, 174, 1});
            put("Sona", new int[] {31, 46, 235, 113, 1});
            put("Soraka", new int[] {19, 32, 261, 123, 1});
            put("Swain", new int[] {56, 22, 236, 109, 1});
            put("Sylas", new int[] {86, 34, 222, 109, 0});
            put("Syndra", new int[] {36, 44, 205, 99, 1});
            put("Tahm Kench", new int[] {2, 102, 306, 244, 0});
            put("Taliyah", new int[] {33, 64, 275, 136, 1});
            put("Talon", new int[] {10, 80, 270, 152, 1});
            put("Taric", new int[] {64, 24, 244, 118, 0});
            put("Teemo", new int[] {3, 124, 305, 166, 1});
            put("Thresh", new int[] {4, 104, 219, 114, 1});
            put("Tristana", new int[] {4, 90, 304, 181, 1});
            put("Trundle", new int[] {4, 122, 240, 123, 1});
            put("Tryndamere", new int[] {40, 56, 268, 137, 0});
            put("Twisted Fate", new int[] {16, 73, 263, 128, 1});
            put("Twitch", new int[] {35, 142, 273, 147, 0});
            put("Udyr", new int[] {2, 239, 274, 129, 1});
            put("Urgot", new int[] {12, 35, 281, 155, 1});
            put("Varus", new int[] {38, 35, 238, 115, 1});
            put("Vayne", new int[] {65, 106, 243, 131, 1});
            put("Veigar", new int[] {2, 119, 281, 136, 0});
            put("Vel'Koz", new int[] {1, 132, 307, 197, 1});
            put("Vi", new int[] {51, 89, 217, 111, 0});
            put("Viktor", new int[] {55, 67, 187, 99, 1});
            put("Vladimir", new int[] {77, 49, 229, 102, 1});
            put("Volibear", new int[] {1, 36, 233, 99, 1});
            put("Warwick", new int[] {10, 157, 298, 168, 1});
            put("Wukong", new int[] {11, 105, 218, 115, 1});
            put("Xayah", new int[] {30, 120, 222, 101, 1});
            put("Xerath", new int[] {49, 46, 210, 85, 1});
            put("Xin Zhao", new int[] {24, 50, 236, 106, 1});
            put("Yasuo", new int[] {33, 112, 218, 111, 1});
            put("Yorick", new int[] {40, 25, 219, 116, 1});
            put("Yuumi", new int[] {2, 74, 306, 139, 0});
            put("Zac", new int[] {29, 246, 279, 125, 1});
            put("Zed", new int[] {84, 65, 215, 97, 0});
            put("Ziggs", new int[] {4, 142, 304, 194, 1});
            put("Zilean", new int[] {30, 86, 212, 91, 1});
            put("Zoe", new int[] {25, 76, 283, 152, 1});
            put("Zyra", new int[] {12, 103, 212, 94, 1});
        }
    };

    public static int[] GetCrop (String name) {
        int[] temp;
        temp = champ_map.get(name);
        return temp;
    }
}
