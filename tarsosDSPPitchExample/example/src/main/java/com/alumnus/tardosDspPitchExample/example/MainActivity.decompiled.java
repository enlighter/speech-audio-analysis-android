package com.alumnus.tarsosDspPitchExample.example;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import com.alumnus.tarsosDspPitchExample.example.R.id;
import com.alumnus.tarsosDspPitchExample.example.R.layout;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 1, 7},
   bv = {1, 0, 2},
   k = 1,
   d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J#\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000e2\u000e\u0010\u000f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u0006¢\u0006\u0002\u0010\u0010J\u0012\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0014J-\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u000b2\u000e\u0010\u000f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u00062\u0006\u0010\u0017\u001a\u00020\u0018H\u0016¢\u0006\u0002\u0010\u0019J\u000e\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u001cJ\u0006\u0010\u001d\u001a\u00020\u0012R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082D¢\u0006\u0002\n\u0000¨\u0006\u001e"},
   d2 = {"Lcom/alumnus/tarsosDspPitchExample/example/MainActivity;", "Landroid/support/v7/app/AppCompatActivity;", "()V", "TAG", "", "audioPermissions", "", "[Ljava/lang/String;", "hasRequiredAudioPermissions", "", "requestIdRequiredAudioPermissions", "", "hasPermissions", "context", "Landroid/content/Context;", "permissions", "(Landroid/content/Context;[Ljava/lang/String;)Z", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onRequestPermissionsResult", "requestCode", "grantResults", "", "(I[Ljava/lang/String;[I)V", "processPitch", "pitchInHz", "", "showPitch", "production sources for module example"}
)
public final class MainActivity extends AppCompatActivity {
   private final String TAG = "MainActivity";
   private boolean hasRequiredAudioPermissions;
   private final String[] audioPermissions;
   private final int requestIdRequiredAudioPermissions;
   private HashMap _$_findViewCache;

   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setContentView(layout.activity_main);
      if(!this.hasPermissions((Context)this, this.audioPermissions)) {
         ActivityCompat.requestPermissions((Activity)this, this.audioPermissions, this.requestIdRequiredAudioPermissions);
      } else {
         this.showPitch();
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
         this.showPitch();
      default:
      }
   }

   public final void showPitch() {
      AudioDispatcher dispatcher = AudioDispatcherFactory.INSTANCE.fromDefaultMicrophone(22050, 1024, 0);
      <undefinedtype> pdh = new PitchDetectionHandler() {
         public void handlePitch(@Nullable PitchDetectionResult p0, @Nullable AudioEvent p1) {
            final Float pitchInHz = p0 != null?Float.valueOf(p0.getPitch()):null;
            Log.d(MainActivity.this.TAG, "Current value: " + String.valueOf(pitchInHz));
            MainActivity.this.runOnUiThread((Runnable)(new Runnable() {
               public final void run() {
                  MainActivity var10000 = MainActivity.this;
                  Float var10001 = pitchInHz;
                  if(pitchInHz == null) {
                     Intrinsics.throwNpe();
                  }

                  var10000.processPitch(var10001.floatValue());
               }
            }));
         }
      };
      PitchProcessor pitchProcessor = new PitchProcessor(PitchEstimationAlgorithm.YIN, 22050.0F, 1024, (PitchDetectionHandler)pdh);
      dispatcher.addAudioProcessor((AudioProcessor)pitchProcessor);
      Thread audioThread = new Thread((Runnable)dispatcher, "Audio Thread");
      audioThread.start();
   }

   public final void processPitch(float pitchInHz) {
      ((TextView)this._$_findCachedViewById(id.pitchText)).setText((CharSequence)("" + pitchInHz));
      if(pitchInHz >= (float)110 && (double)pitchInHz < 123.47D) {
         ((TextView)this._$_findCachedViewById(id.noteText)).setText((CharSequence)"A");
      } else if((double)pitchInHz >= 123.47D && (double)pitchInHz < 130.81D) {
         ((TextView)this._$_findCachedViewById(id.noteText)).setText((CharSequence)"B");
      } else if((double)pitchInHz >= 130.81D && (double)pitchInHz < 146.83D) {
         ((TextView)this._$_findCachedViewById(id.noteText)).setText((CharSequence)"C");
      } else if((double)pitchInHz >= 146.83D && (double)pitchInHz < 164.81D) {
         ((TextView)this._$_findCachedViewById(id.noteText)).setText((CharSequence)"D");
      } else if((double)pitchInHz >= 164.81D && (double)pitchInHz <= 174.61D) {
         ((TextView)this._$_findCachedViewById(id.noteText)).setText((CharSequence)"E");
      } else if((double)pitchInHz >= 174.61D && pitchInHz < (float)185) {
         ((TextView)this._$_findCachedViewById(id.noteText)).setText((CharSequence)"F");
      } else if(pitchInHz >= (float)185 && pitchInHz < (float)196) {
         ((TextView)this._$_findCachedViewById(id.noteText)).setText((CharSequence)"G");
      }

   }

   public MainActivity() {
      Object[] elements$iv = (Object[])(new String[]{"android.permission.RECORD_AUDIO"});
      this.audioPermissions = (String[])elements$iv;
      this.requestIdRequiredAudioPermissions = 1;
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

