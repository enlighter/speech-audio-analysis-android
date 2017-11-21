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
        mv = {1, 1, 8},
        bv = {1, 0, 2},
        k = 1,
        d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0017\n\u0000\n\u0002\u0010\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001)B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0010\u0010!\u001a\u00020\"2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0006\u0010#\u001a\u00020$J\u0006\u0010%\u001a\u00020\u0010J\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010&\u001a\u00020\u000eJ\u0006\u0010'\u001a\u00020(R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\tX\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082.¢\u0006\u0002\n\u0000¨\u0006*"},
        d2 = {"Lcom/alumnus/speechaudioanalysis/SoundRecorderProcessor;", "", "()V", "TAG", "", "kotlin.jvm.PlatformType", "audioDispatcherThread", "Ljava/lang/Thread;", "audioSampleRate", "", "getAudioSampleRate", "()I", "bufferSize", "isRecording", "", "pitch", "", "pitchHandler", "Lbe/tarsos/dsp/pitch/PitchDetectionHandler;", "recorder", "Landroid/media/AudioRecord;", "tdspAudioStream", "Lbe/tarsos/dsp/io/TarsosDSPAudioInputStream;", "tdspDispatcher", "Lbe/tarsos/dsp/AudioDispatcher;", "tdspFormat", "Lbe/tarsos/dsp/io/TarsosDSPAudioFormat;", "tdspPitchProcessor", "Lbe/tarsos/dsp/AudioProcessor;", "calculateDBrms", "", "data", "", "calculateRMS", "", "getAmplitude", "Lcom/alumnus/speechaudioanalysis/SoundRecorderProcessor$amplitudeResult;", "getPitch", "start", "stop", "", "amplitudeResult", "production sources for module app"}
)
public final class SoundRecorderProcessor {
   private final int audioSampleRate = '걄';
   private AudioRecord recorder;
   private int bufferSize;
   private boolean isRecording;
   private final String TAG = SoundRecorderProcessor.class.getName();
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
   public final SoundRecorderProcessor.amplitudeResult getAmplitude() {
      if(!this.isRecording) {
         return new SoundRecorderProcessor.amplitudeResult(0, 0.0D);
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
         double amplitudeDB = this.calculateDBrms(result$iv);
         return new SoundRecorderProcessor.amplitudeResult(amplitudeRMS, amplitudeDB);
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

   private final double calculateDBrms(short[] data) {
      double sum = 0.0D;

      for(int var5 = 0; var5 < data.length; ++var5) {
         short num = data[var5];
         double y = (double)num / 32768.0D;
         sum += y * y;
      }

      double rms = Math.sqrt(sum / (double)data.length);
      return 20.0D * Math.log10(rms);
   }

   public SoundRecorderProcessor() {
      this.bufferSize = AudioRecord.getMinBufferSize(this.audioSampleRate, 1, 2);
      this.isRecording = false;
   }

   // $FF: synthetic method
   public static final float access$getPitch$p(SoundRecorderProcessor $this) {
      return $this.pitch;
   }

   @Metadata(
           mv = {1, 1, 8},
           bv = {1, 0, 2},
           k = 1,
           d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0006\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0015"},
           d2 = {"Lcom/alumnus/speechaudioanalysis/SoundRecorderProcessor$amplitudeResult;", "", "ampRMS", "", "DBrms", "", "(SD)V", "getDBrms", "()D", "getAmpRMS", "()S", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "production sources for module app"}
   )
   public static final class amplitudeResult {
      private final short ampRMS;
      private final double DBrms;

      public final short getAmpRMS() {
         return this.ampRMS;
      }

      public final double getDBrms() {
         return this.DBrms;
      }

      public amplitudeResult(short ampRMS, double DBrms) {
         this.ampRMS = ampRMS;
         this.DBrms = DBrms;
      }

      public final short component1() {
         return this.ampRMS;
      }

      public final double component2() {
         return this.DBrms;
      }

      @NotNull
      public final SoundRecorderProcessor.amplitudeResult copy(short ampRMS, double DBrms) {
         return new SoundRecorderProcessor.amplitudeResult(ampRMS, DBrms);
      }

      // $FF: synthetic method
      // $FF: bridge method
      @NotNull
      public static SoundRecorderProcessor.amplitudeResult copy$default(SoundRecorderProcessor.amplitudeResult var0, short var1, double var2, int var4, Object var5) {
         if((var4 & 1) != 0) {
            var1 = var0.ampRMS;
         }

         if((var4 & 2) != 0) {
            var2 = var0.DBrms;
         }

         return var0.copy(var1, var2);
      }

      public String toString() {
         return "amplitudeResult(ampRMS=" + this.ampRMS + ", DBrms=" + this.DBrms + ")";
      }

      public int hashCode() {
         int var10000 = this.ampRMS * 31;
         long var10001 = Double.doubleToLongBits(this.DBrms);
         return var10000 + (int)(var10001 ^ var10001 >>> 32);
      }

      public boolean equals(Object var1) {
         if(this != var1) {
            if(var1 instanceof SoundRecorderProcessor.amplitudeResult) {
               SoundRecorderProcessor.amplitudeResult var2 = (SoundRecorderProcessor.amplitudeResult)var1;
               if(this.ampRMS == var2.ampRMS && Double.compare(this.DBrms, var2.DBrms) == 0) {
                  return true;
               }
            }

            return false;
         } else {
            return true;
         }
      }
   }
}
