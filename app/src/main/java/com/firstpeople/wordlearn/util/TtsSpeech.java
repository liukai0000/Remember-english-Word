package com.firstpeople.wordlearn.util;

import java.util.Locale;



import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class TtsSpeech implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private Activity context;
    private boolean ttsStatus=false;

    public TtsSpeech(Activity context) {
        this.context = context;

    }

    public TextToSpeech getTts() {
        if(tts == null) {
            tts = new TextToSpeech(context, this);
        }
        return tts;
    }

    public void close()
    {
        if(tts != null)
        //activityæš‚åœæ—¶ä¹Ÿåœæ­¢TTS
        {
            tts.shutdown();
        }
    }

    public boolean getTtsStatus() {
        return ttsStatus;
    }

    public void setTtsStatus(boolean ttsStatus) {
        this.ttsStatus = ttsStatus;
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            float rate=	Config.init().getCanSpeechSpeed();
            tts.setSpeechRate(rate);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                ttsStatus=false;
                Config.init().setIsTtsOk(false);
            } else {
                ttsStatus=true;
                Log.e("speech_init", "success");
                Config.init().setIsTtsOk(true);
            }
        } else {
            ttsStatus=false;
            Config.init().setIsTtsOk(false);
//                	Toast.makeText(context, "å‘éŸ³åˆå§‹åŒ–å¤±è´¥ï¼Œè¯·å®‰è£…TTS ï¼?, Toast.LENGTH_SHORT).show();

        }
    }

}
