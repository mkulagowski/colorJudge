package mkk13.colorjudge.Activities;

import java.util.List;

import mkk13.colorjudge.Color;
import mkk13.colorjudge.ColorDatabase;
import mkk13.colorjudge.Utils;

/**
 * Created by mkk-1 on 08/04/2017.
 */

public class LearnSimilarActivity extends LearnRandomActivity {

    @Override
    protected List<Color> getRandomColors() {
        List<String> bases = ColorDatabase.getInstance().getColorBases();
        int idx = (int) (Math.random() * bases.size());
        List<Color> cols = ColorDatabase.getInstance().getColorsPerBase(bases.get(idx));
        return Utils.shuffle(cols, 4);
    }
}
