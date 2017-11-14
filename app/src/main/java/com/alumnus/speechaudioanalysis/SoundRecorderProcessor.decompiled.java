package com.alumnus.speechaudioanalysis;

import android.media.AudioRecord;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.util.Log;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.android.AndroidAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 1, 7},
   bv = {1, 0, 2},
   k = 1,
   d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0017\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0006\u0010 \u001a\u00020\u001fJ\u0006\u0010!\u001a\u00020\u000fJ\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\"\u001a\u00020\rJ\u0006\u0010#\u001a\u00020$R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\bX\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082.¢\u0006\u0002\n\u0000¨\u0006%"},
   d2 = {"Lcom/alumnus/speechaudioanalysis/SoundRecorderProcessor;", "", "()V", "TAG", "", "audioDispatcherThread", "Ljava/lang/Thread;", "audioSampleRate", "", "getAudioSampleRate", "()I", "bufferSize", "isRecording", "", "pitch", "", "pitchHandler", "Lbe/tarsos/dsp/pitch/PitchDetectionHandler;", "recorder", "Landroid/media/AudioRecord;", "tdspAudioStream", "Lbe/tarsos/dsp/io/TarsosDSPAudioInputStream;", "tdspDispatcher", "Lbe/tarsos/dsp/AudioDispatcher;", "tdspFormat", "Lbe/tarsos/dsp/io/TarsosDSPAudioFormat;", "tdspPitchProcessor", "Lbe/tarsos/dsp/AudioProcessor;", "calculateRMS", "", "data", "", "getAmplitude", "getPitch", "start", "stop", "", "production sources for module app"}
)
public final class SoundRecorderProcessor {
   private final int audioSampleRate = '걄';
   private AudioRecord recorder;
   private int bufferSize;
   private boolean isRecording;
   private final String TAG = "SoundRecorderProcessor";
   private float pitch;
   private final PitchDetectionHandler pitchHandler = (PitchDetectionHandler)(new PitchDetectionHandler() {
      public final void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
         SoundRecorderProcessor.this.pitch = pitchDetectionResult.getPitch();
      }
   });
   private TarsosDSPAudioFormat tdspFormat;
   private TarsosDSPAudioInputStream tdspAudioStream;
   private AudioDispatcher tdspDispatcher;
   private AudioProcessor tdspPitchProcessor;
   private Thread audioDispatcherThread;

   public final int getAudioSampleRate() {
      return this.audioSampleRate;
   }

   public final boolean start() {
      try {
         this.recorder = new AudioRecord(6, this.audioSampleRate, 16, 2, this.bufferSize);
      } catch (Exception var2) {
         Log.e(this.TAG, "Couldn't create audioRecord instance", (Throwable)var2);
         return false;
      }

      AudioRecord var10000;
      try {
         this.tdspFormat = new TarsosDSPAudioFormat((float)this.audioSampleRate, 16, 1, true, false);
         AndroidAudioInputStream var10001 = new AndroidAudioInputStream;
         AudioRecord var10003 = this.recorder;
         TarsosDSPAudioFormat var10004 = this.tdspFormat;
         if(this.tdspFormat == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tdspFormat");
         }

         var10001.<init>(var10003, var10004);
         this.tdspAudioStream = (TarsosDSPAudioInputStream)var10001;
         var10000 = this.recorder;
         if(this.recorder != null) {
            var10000.startRecording();
         }

         this.isRecording = true;
         AudioDispatcher var6 = new AudioDispatcher;
         TarsosDSPAudioInputStream var8 = this.tdspAudioStream;
         if(this.tdspAudioStream == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tdspAudioStream");
         }

         var6.<init>(var8, this.bufferSize, 0);
         this.tdspDispatcher = var6;
         this.tdspPitchProcessor = (AudioProcessor)(new PitchProcessor(PitchEstimationAlgorithm.YIN, (float)this.audioSampleRate, this.bufferSize, this.pitchHandler));
         AudioDispatcher var4 = this.tdspDispatcher;
         if(this.tdspDispatcher == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tdspDispatcher");
         }

         AudioProcessor var7 = this.tdspPitchProcessor;
         if(this.tdspPitchProcessor == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tdspPitchProcessor");
         }

         var4.addAudioProcessor(var7);
      } catch (Exception var3) {
         Log.e(this.TAG, "Couldn't start recording Audio stream: " + (this.recorder != null?Integer.valueOf(this.recorder.getState()):null), (Throwable)var3);
         return false;
      }

      if(this.recorder != null) {
         var10000 = this.recorder;
         if(this.recorder != null) {
            if(var10000.getState() == 1) {
               Integer var5 = this.recorder != null?Integer.valueOf(this.recorder.getAudioSessionId()):null;
               if(var5 == null) {
                  Intrinsics.throwNpe();
               }

               NoiseSuppressor.create(var5.intValue());
               var5 = this.recorder != null?Integer.valueOf(this.recorder.getAudioSessionId()):null;
               if(var5 == null) {
                  Intrinsics.throwNpe();
               }

               AcousticEchoCanceler.create(var5.intValue());
               var5 = this.recorder != null?Integer.valueOf(this.recorder.getAudioSessionId()):null;
               if(var5 == null) {
                  Intrinsics.throwNpe();
               }

               AutomaticGainControl.create(var5.intValue());
            }
         }
      }

      return true;
   }

   public final void stop() {
      AudioRecord var10000 = this.recorder;
      if(this.recorder != null) {
         var10000.stop();
      }

      var10000 = this.recorder;
      if(this.recorder != null) {
         var10000.release();
      }

      this.recorder = (AudioRecord)null;
      this.isRecording = false;
   }

   public final boolean isRecording() {
      return this.isRecording;
   }

   @NotNull
   public final short[] getAmplitude() {
      if(!this.isRecording) {
         return new short[]{0, 0};
      } else {
         int size$iv = this.bufferSize;
         short[] result$iv = new short[size$iv];
         int i$iv = 0;

         for(int var5 = result$iv.length; i$iv < var5; ++i$iv) {
            byte var11 = 0;
            result$iv[i$iv] = var11;
         }

         double average = 0.0D;
         AudioRecord var10000 = this.recorder;
         if(this.recorder != null) {
            var10000.read(result$iv, 0, this.bufferSize);
         }

         short amplitudeRMS = this.calculateRMS(result$iv);
         short amplitudeDB = 0;
         return new short[]{amplitudeRMS, amplitudeDB};
      }
   }

   public final float getPitch() {
      if(this.audioDispatcherThread == null) {
         Thread var10001 = new Thread;
         AudioDispatcher var10003 = this.tdspDispatcher;
         if(this.tdspDispatcher == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tdspDispatcher");
         }

         var10001.<init>((Runnable)var10003, "Pitch Thread");
         this.audioDispatcherThread = var10001;
         Thread var10000 = this.audioDispatcherThread;
         if(this.audioDispatcherThread != null) {
            var10000.start();
         }
      }

      return this.pitch;
   }

   private final short calculateRMS(short[] data) {
      double sumOfSquares = 0.0D;

      for(int var5 = 0; var5 < data.length; ++var5) {
         short num = data[var5];
         sumOfSquares += (double)(num * num);
      }

      double meanSquare = sumOfSquares / (double)data.length;
      return (short)((int)Math.sqrt(meanSquare));
   }

   public SoundRecorderProcessor() {
      this.bufferSize = AudioRecord.getMinBufferSize(this.audioSampleRate, 1, 2);
      this.isRecording = false;
   }

   // $FF: synthetic method
   public static final float access$getPitch$p(SoundRecorderProcessor $this) {
      return $this.pitch;
   }
}
