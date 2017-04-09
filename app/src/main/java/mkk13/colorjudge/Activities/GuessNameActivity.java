package mkk13.colorjudge.Activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import mkk13.colorjudge.Adapters.ScoreAdapter;
import mkk13.colorjudge.ColorDatabase;
import mkk13.colorjudge.ColorDetailsListener;
import mkk13.colorjudge.ColorUtils;
import mkk13.colorjudge.R;
import mkk13.colorjudge.Score;
import mkk13.colorjudge.Utils;


/**
 * Created by mkk-1 on 26/03/2017.
 */

public class GuessNameActivity extends Activity implements View.OnClickListener {
    private ScoreAdapter customAdapter;
    private EditText textEdit;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.namepanel);
        customAdapter = new ScoreAdapter(this, R.layout.scoretemplate);

        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(customAdapter);
        list.setOnItemClickListener(new ColorDetailsListener(this));

        Button captureBtn = (Button)findViewById(R.id.speech_capture_btn);
        captureBtn.setOnClickListener(this);

        textEdit = (EditText) findViewById(R.id.speech_textedit);
        textEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence text, int start,
                                      int before, int count) {
                if(text.length() != 0) {
                    ArrayList<Score> results = ColorUtils.getMatchingNames(text.toString().toLowerCase(), ColorDatabase.getInstance().getColors().values());
                    customAdapter.clear();
                    customAdapter.addAll(results.subList(0, Utils.COLORS_IN_LIST));
                } else {
                    customAdapter.clear();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.speech_capture_btn) {
            promptSpeechInput();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Utils.SOUND_CAPTURE_INTENT && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            textEdit.setText(result.get(0));
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Utils.LANGUAGE.getLocale());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, Utils.SOUND_CAPTURE_INTENT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                           getString(R.string.speech_not_supported),
                           Toast.LENGTH_SHORT
            ).show();
        }
    }
}