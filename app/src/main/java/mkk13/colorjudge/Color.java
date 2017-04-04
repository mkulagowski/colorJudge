package mkk13.colorjudge;

import org.json.simple.JSONObject;

/**
 * Created by mkk-1 on 25/03/2017.
 */
public class Color implements java.io.Serializable {
    public String name_pl;
    public String name_eng;
    public String hex;
    public int[] rgb;
    public float[] lab;
    public float[] xyz;

    public Color() {
        name_pl = "";
        name_eng = "";
        hex = "";
        rgb = new int[] {0, 0, 0};
        lab = new float[] {0, 0, 0};
        xyz = new float[] {0, 0, 0};
    }

    public Color(String hex, String namePl, String nameEng) {
        this.name_pl = namePl;
        this.name_eng = nameEng;
        initFromHex(hex);
    }

    private void initFromHex(String hex) {
        this.hex = hex.toUpperCase();
        this.rgb = ColorConversions.hex2rgb(hex);
        this.xyz = ColorConversions.rgb2xyz(this.rgb);
        this.lab = ColorConversions.xyz2lab(this.xyz);
    }

    public Color(JSONObject obj) {
        if (obj.containsKey("name_pl")) {
            this.name_pl = obj.get("name_pl").toString();
        }

        if (obj.containsKey("name_eng")) {
            this.name_eng = obj.get("name_eng").toString();
        }

        if (obj.containsKey("color")) {
            String colHex = obj.get("color").toString();
            if (!obj.containsKey("lab") || !obj.containsKey("xyz")) {
                initFromHex(colHex);
            } else {
                this.hex = colHex.toUpperCase();
                this.rgb = ColorConversions.hex2rgb(hex);
                JSONObject lab = (JSONObject) obj.get("lab");
                this.lab = new float[] {Float.valueOf(lab.get("l").toString()),
                                        Float.valueOf(lab.get("a").toString()),
                                        Float.valueOf(lab.get("b").toString())};
                JSONObject xyz = (JSONObject) obj.get("xyz");
                this.xyz = new float[] {Float.valueOf(lab.get("x").toString()),
                                        Float.valueOf(lab.get("y").toString()),
                                        Float.valueOf(lab.get("z").toString())};
            }
        }
    }

    public String[] getHexStringDetails() {
        return new String[] {"", hex, ""};
    }

    public String[] getRgbStringDetails() {
        String[] res = new String[] {"R: ", "G: ", "B: "};
        for (int i = 0; i < 3; i++) {
            res[i] += rgb[i];
        }
        return res;
    }

    public String[] getLabStringDetails() {
        String[] res = new String[] {"L: ", "a: ", "b: "};
        for (int i = 0; i < 3; i++) {
            res[i] += lab[i];
        }
        return res;
    }

    public String[] getXyzStringDetails() {
        String[] res = new String[] {"X: ", "Y: ", "Z: "};
        for (int i = 0; i < 3; i++) {
            res[i] += xyz[i];
        }
        return res;
    }

    public String[] getHsvStringDetails() {
        float[] hsv = ColorConversions.rgb2hsv(rgb);
        String[] res = new String[] {"H: ", "S: ", "V: "};
        for (int i = 0; i < 3; i++) {
            res[i] += hsv[i];
        }
        return res;
    }
}
