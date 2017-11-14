package com.alumnus.tarsosDspPitchExample.example;

import android.media.AudioRecord;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.PipedAudioStream;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.android.AndroidAudioInputStream;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 1, 7},
   bv = {1, 0, 2},
   k = 1,
   d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006J&\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006¨\u0006\r"},
   d2 = {"Lcom/alumnus/tarsosDspPitchExample/example/AudioDispatcherFactory;", "", "()V", "fromDefaultMicrophone", "Lbe/tarsos/dsp/AudioDispatcher;", "sampleRate", "", "audioBufferSize", "bufferOverlap", "fromPipe", "source", "", "targetSampleRate", "production sources for module example"}
)
public final class AudioDispatcherFactory {
   public static final AudioDispatcherFactory INSTANCE;

   @NotNull
   public final AudioDispatcher fromDefaultMicrophone(int sampleRate, int audioBufferSize, int bufferOverlap) {
      int minAudioBufferSize = AudioRecord.getMinBufferSize(sampleRate, 16, 2);
      int minAudioBufferSizeInSamples = minAudioBufferSize / 2;
      if(minAudioBufferSizeInSamples <= audioBufferSize) {
         AudioRecord audioInputStream = new AudioRecord(1, sampleRate, 16, 2, audioBufferSize * 2);
         TarsosDSPAudioFormat format = new TarsosDSPAudioFormat((float)sampleRate, 16, 1, true, false);
         AndroidAudioInputStream audioStream = new AndroidAudioInputStream(audioInputStream, format);
         audioInputStream.startRecording();
         return new AudioDispatcher((TarsosDSPAudioInputStream)audioStream, audioBufferSize, bufferOverlap);
      } else {
         throw (Throwable)(new IllegalArgumentException("Buffer size too small should be at least " + minAudioBufferSize * 2));
      }
   }

   @NotNull
   public final AudioDispatcher fromPipe(@NotNull String source, int targetSampleRate, int audioBufferSize, int bufferOverlap) {
      Intrinsics.checkParameterIsNotNull(source, "source");
      PipedAudioStream f = new PipedAudioStream(source);
      TarsosDSPAudioInputStream audioStream = f.getMonoStream(targetSampleRate, 0.0D);
      return new AudioDispatcher(audioStream, audioBufferSize, bufferOverlap);
   }

   private AudioDispatcherFactory() {
      INSTANCE = (AudioDispatcherFactory)this;
   }

   static {
      new AudioDispatcherFactory();
   }
}
