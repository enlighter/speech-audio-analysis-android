package com.alumnus.speechaudioanalysis;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.alumnus.speechaudioanalysis.R.id;
import com.alumnus.speechaudioanalysis.SoundRecorderProcessor.amplitudeResult;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref.FloatRef;
import kotlin.jvm.internal.Ref.ObjectRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
        mv = {1, 1, 8},
        bv = {1, 0, 2},
        k = 1,
        d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0004\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J#\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u001d2\u000e\u0010\u001e\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0\r¢\u0006\u0002\u0010\u001fJ\u0012\u0010 \u001a\u00020\u00162\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u0012\u0010#\u001a\u00020\u00162\b\u0010$\u001a\u0004\u0018\u00010%H\u0014J-\u0010&\u001a\u00020\u00162\u0006\u0010'\u001a\u00020\u00052\u000e\u0010\u001e\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0\r2\u0006\u0010(\u001a\u00020)H\u0016¢\u0006\u0002\u0010*J\b\u0010+\u001a\u00020\u0016H\u0002J\b\u0010,\u001a\u00020\u0016H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082D¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00020\n0\rX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000eR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0005X\u0082D¢\u0006\u0002\n\u0000¨\u0006-"},
        d2 = {"Lcom/alumnus/speechaudioanalysis/MainActivity;", "Landroid/support/v7/app/AppCompatActivity;", "Landroid/view/View$OnClickListener;", "()V", "IDLE", "", "RUNNING", "SoundObject", "Lcom/alumnus/speechaudioanalysis/SoundRecorderProcessor;", "TAG", "", "kotlin.jvm.PlatformType", "audioPermissions", "", "[Ljava/lang/String;", "audioProcessThread", "Ljava/lang/Thread;", "currentState", "hasRequiredAudioPermissions", "", "requestIdRequiredAudioPermissions", "displayAcquiredValues", "", "pitchInHz", "", "amplitude", "Lcom/alumnus/speechaudioanalysis/SoundRecorderProcessor$amplitudeResult;", "hasPermissions", "context", "Landroid/content/Context;", "permissions", "(Landroid/content/Context;[Ljava/lang/String;)Z", "onClick", "v", "Landroid/view/View;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onRequestPermissionsResult", "requestCode", "grantResults", "", "(I[Ljava/lang/String;[I)V", "resetDisplayValues", "runOnAudioProcessThread", "production sources for module app"}
)
public final class MainActivity extends AppCompatActivity implements OnClickListener {
   private SoundRecorderProcessor SoundObject;
   private final String TAG = MainActivity.class.getName();
   private final int IDLE;
   private final int RUNNING = 1;
   private final int requestIdRequiredAudioPermissions = 1;
   private final String[] audioPermissions = new String[]{"android.permission.RECORD_AUDIO", "android.permission.MODIFY_AUDIO_SETTINGS"};
   private boolean hasRequiredAudioPermissions;
   private Thread audioProcessThread;
   private int currentState;
   private HashMap _$_findViewCache;

   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setContentView(2131296282);
      ((Button)this._$_findCachedViewById(id.mainButton)).setOnClickListener((OnClickListener)this);
      ((Button)this._$_findCachedViewById(id.mainButton)).setText(this.getText(2131492895));
      if(!this.hasPermissions((Context)this, this.audioPermissions)) {
         ActivityCompat.requestPermissions((Activity)this, this.audioPermissions, this.requestIdRequiredAudioPermissions);
      }

   }

   public final boolean hasPermissions(@NotNull Context context, @NotNull String[] permissions) {
      Intrinsics.checkParameterIsNotNull(context, "context");
      Intrinsics.checkParameterIsNotNull(permissions, "permissions");

      for(int var4 = 0; var4 < permissions.length; ++var4) {
         String permission = permissions[var4];
         if(ActivityCompat.checkSelfPermission(context, permission) != 0) {
            this.hasRequiredAudioPermissions = false;
            return false;
         }
      }

      this.hasRequiredAudioPermissions = true;
      return true;
   }

   public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
      Intrinsics.checkParameterIsNotNull(permissions, "permissions");
      Intrinsics.checkParameterIsNotNull(grantResults, "grantResults");
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      switch(requestCode) {
         case 1:
            this.hasRequiredAudioPermissions = true;
         default:
      }
   }

   public void onClick(@Nullable View v) {
      if(v != null) {
         if(v.getId() == ((Button)this._$_findCachedViewById(id.mainButton)).getId()) {
            if(!this.hasRequiredAudioPermissions) {
               Toast.makeText((Context)this, (CharSequence)"Without required audio permissions, this app cannot function", 1).show();
               this.currentState = this.IDLE;
               return;
            }

            this.currentState = this.RUNNING;
            if(this.SoundObject == null) {
               this.SoundObject = new SoundRecorderProcessor();
            }

            if(this.SoundObject != null) {
               SoundRecorderProcessor var10000 = this.SoundObject;
               if(this.SoundObject == null) {
                  Intrinsics.throwNpe();
               }

               if(!var10000.isRecording()) {
                  var10000 = this.SoundObject;
                  if(this.SoundObject == null) {
                     Intrinsics.throwNpe();
                  }

                  if(!var10000.start()) {
                     Toast.makeText((Context)this, (CharSequence)"Couldn't acquire record stream!", 1).show();
                  }

                  ((Button)this._$_findCachedViewById(id.mainButton)).setText(this.getText(2131492896));
                  if(this.audioProcessThread == null) {
                     this.audioProcessThread = new Thread((Runnable)(new Runnable() {
                        public final void run() {
                           MainActivity.this.runOnAudioProcessThread();
                        }
                     }));
                     Thread var2 = this.audioProcessThread;
                     if(this.audioProcessThread != null) {
                        var2.start();
                     }
                  }
               } else {
                  this.currentState = this.IDLE;
                  var10000 = this.SoundObject;
                  if(this.SoundObject == null) {
                     Intrinsics.throwNpe();
                  }

                  var10000.stop();
                  ((Button)this._$_findCachedViewById(id.mainButton)).setText(this.getText(2131492895));
                  this.resetDisplayValues();
               }
            } else {
               Toast.makeText((Context)this, (CharSequence)"Did not get SoundObject", 1).show();
               this.currentState = this.IDLE;
            }
         }
      }

   }

   private final void runOnAudioProcessThread() {
      while(true) {
         SoundRecorderProcessor var10000 = this.SoundObject;
         if(this.SoundObject == null) {
            Intrinsics.throwNpe();
         }

         if(!var10000.isRecording() || this.currentState != this.RUNNING) {
            return;
         }

         final ObjectRef currentAmplitude = new ObjectRef();
         SoundRecorderProcessor var10001 = this.SoundObject;
         if(this.SoundObject == null) {
            Intrinsics.throwNpe();
         }

         currentAmplitude.element = var10001.getAmplitude();
         Log.d(this.TAG, "Current amplitude: " + ((amplitudeResult)currentAmplitude.element).toString());
         final FloatRef currentPitch = new FloatRef();
         var10001 = this.SoundObject;
         if(this.SoundObject == null) {
            Intrinsics.throwNpe();
         }

         currentPitch.element = var10001.getPitch();
         Log.d(this.TAG, "Current pitch: " + String.valueOf(currentPitch.element));
         this.runOnUiThread((Runnable)(new Runnable() {
            public final void run() {
               MainActivity.this.displayAcquiredValues(currentPitch.element, (amplitudeResult)currentAmplitude.element);
            }
         }));
      }
   }

   private final void displayAcquiredValues(float pitchInHz, amplitudeResult amplitude) {
      ((TextView)this._$_findCachedViewById(id.pitchText)).setText((CharSequence)String.valueOf(pitchInHz));
      short ampRMS = amplitude.component1();
      double DBrms = amplitude.component2();
      ((TextView)this._$_findCachedViewById(id.amplitudeText)).setText((CharSequence)String.valueOf(ampRMS));
   }

   private final void resetDisplayValues() {
      this.displayAcquiredValues(0.0F, new amplitudeResult(0, 0.0D));
   }

   public MainActivity() {
      this.currentState = this.IDLE;
   }

   public View _$_findCachedViewById(int var1) {
      if(this._$_findViewCache == null) {
         this._$_findViewCache = new HashMap();
      }

      View var2 = (View)this._$_findViewCache.get(Integer.valueOf(var1));
      if(var2 == null) {
         var2 = this.findViewById(var1);
         this._$_findViewCache.put(Integer.valueOf(var1), var2);
      }

      return var2;
   }

   public void _$_clearFindViewByIdCache() {
      if(this._$_findViewCache != null) {
         this._$_findViewCache.clear();
      }

   }
}
