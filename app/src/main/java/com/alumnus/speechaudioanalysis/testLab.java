package com.alumnus.speechaudioanalysis;

/**
 * Created by sushovan on 2/11/17.
 */

import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionResult;

public class testLab {

    PitchDetectionHandler handler = new PitchDetectionHandler() {
        @Override
        public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                AudioEvent audioEvent) {
            System.out.println(audioEvent.getTimeStamp() + " " + pitchDetectionResult.getPitch());
        }
    };
}
