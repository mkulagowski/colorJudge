package mkk13.colorjudge;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by mkk-1 on 29/03/2017.
 */

public class ColorBase {
    public HashMap<String, Color> colors = new HashMap<>();
    private static ColorBase instance;
    private static Context cont;

    public static ColorBase getInstance() {
        if (instance == null) {
            instance = new ColorBase();
        }
        return instance;
    }

    public static void setContext(Context con) {
        cont = con;
    }

    private ColorBase() {
        JSONParser parser = new JSONParser();
        Object obj = new Object();

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
            colors.put(col.name_pl, col);
        }
    }
}
