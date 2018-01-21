package com.firstpeople.wordlearn.util;

import java.util.Locale;



import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class TtsSpeechCheck implements TextToSpeech.OnInitListener {

        private TextToSpeech tts;
        private Activity context;
        private boolean ttsStatus=true;
        
        public boolean getTtsStatus() {
			return ttsStatus;
		}

		public void setTtsStatus(boolean ttsStatus) {
			this.ttsStatus = ttsStatus;
		}

		public TtsSpeechCheck(Activity context) {
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
	          //activity暂停时也停止TTS  
	      {  
  	    	tts.shutdown();  
	      }  
        }
        
        
        @Override
        public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                        int result = tts.setLanguage(Locale.US);
                        float rate=	Config.init().getCanSpeechSpeed();
                        tts.setSpeechRate(rate);

                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        	ttsStatus=false;
                        	Intent dataIntent = new Intent();  
        	                dataIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);  
        	                context.startActivity(dataIntent);  
        	                
                        } else {
                        	ttsStatus=true;

                                Log.e("speech_init", "success");
                        }
                } else {
                	ttsStatus=false;
	                Intent dataIntent = new Intent();  
	                dataIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);  
	                context.startActivity(dataIntent);  
                }
        }

}
