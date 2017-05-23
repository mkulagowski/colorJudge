package mkk13.colorjudge;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mkk-1 on 25/03/2017.
 */
public class Color implements java.io.Serializable {
    private Map<Utils.Language, String> mNameMap = new HashMap<>();
    private String mHex;
    private String mBase;
    private int[] mRgb;
    private float[] mLab;
    private float[] mXyz;

    public Color() {
        mHex = "";
        mBase = "";
        mRgb = new int[] {0, 0, 0};
        mLab = new float[] {0, 0, 0};
        mXyz = new float[] {0, 0, 0};
    }

    public Color(String hex, String namePl, String nameEng, String mBase) {
        mNameMap.put(Utils.Language.POLISH, namePl);
        mNameMap.put(Utils.Language.ENGLISH, nameEng);
        initFromHex(hex);
    }

    private void initFromHex(String hex) {
        mHex = hex.toUpperCase();
        mRgb = ColorConversions.hex2rgb(hex);
        mXyz = ColorConversions.rgb2xyz(mRgb);
        mLab = ColorConversions.xyz2lab(mXyz);
    }

    public Color(JSONObject obj) {
        if (obj.containsKey("name_pl")) {
            mNameMap.put(Utils.Language.POLISH, obj.get("name_pl").toString());
        }

        if (obj.containsKey("name_eng")) {
            mNameMap.put(Utils.Language.ENGLISH, obj.get("name_eng").toString());
        }

        if (obj.containsKey("baseColor")) {
            mBase = obj.get("baseColor").toString();
        }

        if (obj.containsKey("color")) {
            String colHex = obj.get("color").toString();
            if (!obj.containsKey("lab") || !obj.containsKey("xyz")) {
                initFromHex(colHex);
            } else {
                mHex = colHex.toUpperCase();
                mRgb = ColorConversions.hex2rgb(mHex);
                JSONObject lab = (JSONObject) obj.get("lab");
                mLab = new float[] {Float.valueOf(lab.get("l").toString()),
                                        Float.valueOf(lab.get("a").toString()),
                                        Float.valueOf(lab.get("b").toString())};
                JSONObject xyz = (JSONObject) obj.get("xyz");
                mXyz = new float[] {Float.valueOf(lab.get("x").toString()),
                                        Float.valueOf(lab.get("y").toString()),
                                        Float.valueOf(lab.get("z").toString())};
            }
        }
    }

    public String getName() {
        return mNameMap.get(Utils.LANGUAGE);
    }

    public String getName(Utils.Language lang) {
        return mNameMap.get(lang);
    }

    public String getBase() {
        return mBase;
    }

    public String getHex() {
        return mHex;
    }

    public int[] getRGB() {
        return mRgb;
    }

    public float[] getLAB() {
        return mLab;
    }

    public float[] getXYZ() {
        return mXyz;
    }

    public String[] getHexStringDetails() {
        return new String[] {"", mHex, ""};
    }

    public String[] getRgbStringDetails() {
        String[] res = new String[] {"R: ", "G: ", "B: "};
        for (int i = 0; i < 3; i++) {
            res[i] += mRgb[i];
        }
        return res;
    }

    public String[] getLabStringDetails() {
        String[] res = new String[] {"L: ", "a: ", "b: "};
        for (int i = 0; i < 3; i++) {
            res[i] += String.format(Locale.getDefault(), "%.3f", mLab[i]);
        }
        return res;
    }

    public String[] getXyzStringDetails() {
        String[] res = new String[] {"X: ", "Y: ", "Z: "};
        for (int i = 0; i < 3; i++) {
            res[i] += String.format(Locale.getDefault(), "%.3f", mXyz[i]);
        }
        return res;
    }

    public String[] getHsvStringDetails() {
        float[] hsv = ColorConversions.rgb2hsv(mRgb);
        String[] res = new String[] {"H: ", "S: ", "V: "};
        for (int i = 0; i < 3; i++) {
            res[i] += String.format(Locale.getDefault(), "%.3f", hsv[i]);
        }
        return res;
    }
}
