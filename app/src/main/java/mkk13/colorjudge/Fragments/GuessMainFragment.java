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

import mkk13.colorjudge.Activities.ColorPanel;
import mkk13.colorjudge.DbPanel;
import mkk13.colorjudge.Activities.NamePanel;
import mkk13.colorjudge.R;

/**
 * Created by mkk-1 on 05/04/2017.
 */

public class GuessMainFragment extends Fragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.mainpanel, container, false);
        Activity act = getActivity();


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Activity act = getActivity();
        List<Button> buttonList = new ArrayList<>();
        buttonList.add((Button) act.findViewById(R.id.act_button_color));
        buttonList.add((Button) act.findViewById(R.id.act_button_name));
        buttonList.add((Button) act.findViewById(R.id.act_button_judge));
        buttonList.add((Button) act.findViewById(R.id.act_button_db));

        for (Button btn : buttonList) {
            btn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Class intentClass = null;
        switch(v.getId()){
            case(R.id.act_button_color):
                intentClass = ColorPanel.class;
                break;
            case(R.id.act_button_name):
                intentClass = NamePanel.class;
                break;
            case(R.id.act_button_judge):
                break;
            case(R.id.act_button_db):
                intentClass = DbPanel.class;
                break;
        }

        if (intentClass != null) {
            Intent myIntent = new Intent(getActivity(), intentClass);
            getActivity().startActivity(myIntent);
        }
    }

}
