package mkk13.colorjudge.Activities;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import mkk13.colorjudge.Color;
import mkk13.colorjudge.ColorConversions;
import mkk13.colorjudge.ColorDatabase;
import mkk13.colorjudge.R;
import mkk13.colorjudge.Utils;

/**
 * Created by mkk-1 on 07/04/2017.
 */

public class LearnRandomActivity extends Activity implements View.OnClickListener {

    private static final String SCORE = "Score: ";
    private static final String COL_QUESTION = "Which color is ";
    private static int mScore = 0;
    private static LearnRandomActivity instance;
    private Color primaryColor;
    private List<Button> buttonList = new ArrayList<>();
    private List<ImageView> starsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.learnrandompanel);

        buttonList.add((Button) findViewById(R.id.learnTL));
        buttonList.add((Button) findViewById(R.id.learnTR));
        buttonList.add((Button) findViewById(R.id.learnBL));
        buttonList.add((Button) findViewById(R.id.learnBR));
        for (Button btn : buttonList) {
            btn.setOnClickListener(this);
        }

        starsList.add((ImageView) findViewById(R.id.starTL));
        starsList.add((ImageView) findViewById(R.id.starTR));
        starsList.add((ImageView) findViewById(R.id.starBL));
        starsList.add((ImageView) findViewById(R.id.starBR));

        refreshColors();
    }

    private void refreshColors() {
        // Shuffle colors and pick an array of 4
        List<Color> cols = new ArrayList<Color>(ColorDatabase.getInstance().getColors().values());
        List<Color> colArray = Utils.shuffle(cols, 4);

        // First color will be the 'test color'
        primaryColor = colArray.get((int) (Math.random() * 4));

        colArray = Utils.shuffle(colArray);
        Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein_instant);
        for (int i = 0; i < buttonList.size(); i++) {
            buttonList.get(i).setBackgroundColor(ColorConversions.hex2int(colArray.get(i).getHex()));
            buttonList.get(i).startAnimation(fadein);
        }

        refreshTexts();
    }

    private void refreshTexts() {
        TextView score = (TextView) findViewById(R.id.learn_score);
        TextView quest = (TextView) findViewById(R.id.learn_question);
        score.setText(SCORE + mScore);
        quest.setText(COL_QUESTION + primaryColor.getName() + '?');
    }

    public void onClick(View clicked) {
        final Animation animFadeStar = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.stars_fadeout);
        final Animation animFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        int winTileId = -1;
        for (int i = 0; i < buttonList.size(); i++) {
            if (checkIfPrimary(buttonList.get(i))) {
                winTileId = i;
            } else {
                buttonList.get(i).startAnimation(animFade);
            }
        }

        final int newScore;

        if(buttonList.get(winTileId).getId() == clicked.getId()) {
            final ImageView star = starsList.get(winTileId);
            newScore = mScore + 1;

            Animation animStar = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.stars);
            final Animation animFade2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
            star.startAnimation(animStar);
            animStar.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationRepeat(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    star.startAnimation(animFade2);
                }
            });
        } else {
            newScore = 0;
        }

        buttonList.get(winTileId).startAnimation(animFadeStar);
        animFadeStar.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                LearnRandomActivity.instance.mScore = newScore;
                LearnRandomActivity.instance.refreshColors();
            }
        });
    }

    private Boolean checkIfPrimary(View v) {
        return ((ColorDrawable) v.getBackground()).getColor() == ColorConversions.hex2int(primaryColor.getHex());
    }
}
