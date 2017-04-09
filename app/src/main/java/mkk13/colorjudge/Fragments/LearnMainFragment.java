package mkk13.colorjudge.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import mkk13.colorjudge.Activities.LearnRandomActivity;
import mkk13.colorjudge.Activities.LearnSimilarActivity;
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
                intentClass = LearnSimilarActivity.class;
                break;
        }

        if (intentClass != null) {
            Intent myIntent = new Intent(getActivity(), intentClass);
            getActivity().startActivity(myIntent);
        }
    }
}