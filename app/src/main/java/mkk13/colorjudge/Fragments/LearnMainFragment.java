package mkk13.colorjudge.Fragments;


import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import mkk13.colorjudge.Activities.DatabaseActivity;
import mkk13.colorjudge.Activities.GuessColorActivity;
import mkk13.colorjudge.Activities.GuessNameActivity;
import mkk13.colorjudge.Activities.LearnRandomActivity;
import mkk13.colorjudge.R;

public class LearnMainFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.learnpanel, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Activity act = getActivity();

        List<Button> buttonList = new ArrayList<>();
        buttonList.add((Button) act.findViewById(R.id.act_button_randoms));
        buttonList.add((Button) act.findViewById(R.id.act_button_similars));

        for (Button btn : buttonList) {
            btn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Class intentClass = null;
        switch(v.getId()){
            case(R.id.act_button_randoms):
                intentClass = LearnRandomActivity.class;
                break;
            case(R.id.act_button_similars):

                /*
                ImageView star = (ImageView) getActivity().findViewById(R.id.starOfEternalSuccess);
                Animation anim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.stars);

                star.clearAnimation();
                star.startAnimation(anim);
                //anim.start();*/
                break;
        }

        if (intentClass != null) {
            Intent myIntent = new Intent(getActivity(), intentClass);
            getActivity().startActivity(myIntent);
        }
    }
}