package mkk13.colorjudge;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mkk-1 on 29/03/2017.
 */

public class ColorDatabase {
    public HashMap<Utils.Language, HashMap<String, Color>> colors = new HashMap<>();
    public HashMap<String, List<Color>> colorsPerBase = new HashMap<>();
    private static ColorDatabase instance;
    private static Context cont;

    public static ColorDatabase getInstance() {
        if (instance == null) {
            instance = new ColorDatabase();
        }
        return instance;
    }

    public static void setContext(Context con) {
        cont = con;
    }

    public HashMap<String, Color> getColors() {
        return colors.get(Utils.LANGUAGE);
    }

    public List<Color> getColorsPerBase(String base) {
        return colorsPerBase.get(base);
    }

    public List<String> getColorBases() {
        return new ArrayList<>(colorsPerBase.keySet());
    }

    private ColorDatabase() {
        JSONParser parser = new JSONParser();
        Object obj = new Object();
        colors.put(Utils.Language.POLISH, new HashMap<String, Color>());
        colors.put(Utils.Language.ENGLISH, new HashMap<String, Color>());

        try {
            AssetManager assetManager = cont.getAssets();
            InputStream file = assetManager.open("colors.json");
            InputStreamReader fileReader = new InputStreamReader(file);
            obj = parser.parse(fileReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray jsonObject = (JSONArray) obj;

        for (Object i : jsonObject) {
            JSONObject jo = (JSONObject) i;
            Color col = new Color(jo);
            colors.get(Utils.Language.POLISH).put(col.getName(Utils.Language.POLISH), col);
            colors.get(Utils.Language.ENGLISH).put(col.getName(Utils.Language.ENGLISH), col);
            if (!colorsPerBase.containsKey(col.getBase())) {
                colorsPerBase.put(col.getBase(), new ArrayList<Color>());
            }
            colorsPerBase.get(col.getBase()).add(col);
        }
    }
}
