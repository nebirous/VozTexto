package com.example.nebir.voztexto;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nebir.voztexto.Bot.ChatterBot;
import com.example.nebir.voztexto.Bot.ChatterBotFactory;
import com.example.nebir.voztexto.Bot.ChatterBotSession;
import com.example.nebir.voztexto.Bot.ChatterBotType;

import java.util.ArrayList;
import java.util.Locale;

public class Principal extends AppCompatActivity {

    private final int CTE=1, ESC=2;
    private TextToSpeech tts;
    private String respuesta, text;
    private android.widget.TextView tvEscrito, textView;
    private EditText etEscribir;
    private android.widget.Button btnHablar;
    private android.widget.Button btnEscuchar;
    private android.widget.ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        this.imageButton = (ImageButton) findViewById(R.id.imageButton);
        this.tvEscrito = (TextView) findViewById(R.id.tvEscrito);
        this.textView = (TextView) findViewById(R.id.textView);
    }

    public void escuchar(){
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, CTE);
    }

    public void hablar(View v){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Habla ahora");
        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        startActivityForResult(i, ESC);
    }

    public void enviar(){
        Tarea t= new Tarea();
        t.execute(text);


    }

    public class Tarea extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            ChatterBotFactory factory = new ChatterBotFactory();

            ChatterBot bot1 = null;
            try {
                bot1 = factory.create(ChatterBotType.CLEVERBOT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ChatterBotSession bot1session = bot1.createSession();

//            ChatterBot bot2 = null;
//            try {
//                bot2 = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            ChatterBotSession bot2session = bot2.createSession();

            String s = params[0];


            try {
                return bot1session.think(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String out) {
            escuchar();
            textView.setText(out);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case CTE:
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onInit(int status) {
                            if (status == TextToSpeech.SUCCESS) {
                                respuesta = textView.getText().toString();

                                tts.setLanguage(new Locale("es", "ES"));
                                tts.setLanguage(Locale.getDefault());
                                tts.isLanguageAvailable(Locale.US);
                                tts.isLanguageAvailable(new Locale("spa", "ESP"));
                                tts.speak(respuesta, TextToSpeech.QUEUE_FLUSH, null, null); //api 21
                                tts.speak(respuesta, TextToSpeech.QUEUE_FLUSH, null);
                            } else {
                                etEscribir.setText("No se ha podido ejecutar");
                            }
                        }
                    });
                }
                break;
            case ESC:
                ArrayList<String> textos = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                text = textos.get(0);
                tvEscrito.setText(text);
                enviar();
                break;
            default:
                Intent i = new Intent();
                i.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(i);
                break;
        }


    }
}
