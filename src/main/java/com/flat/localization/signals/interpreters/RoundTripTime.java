package com.flat.localization.signals.interpreters;

import com.flat.localization.util.Const;

/**
 * Created by Jacob Phillips (10/2014)
 */
public final class RoundTripTime implements SignalInterpreter {
    @Override
    public String getName() {
        return "RTT";
    }

    /**
     * This assumes the timestamps have nanosecond precision
     */
    public double fromNanoTime(long aSent, long bReceived, long bSent, long aReceived) {
        long roundTrip = (aReceived - aSent) - (bSent - bReceived);
        double d = (Const.SPEED_OF_LIGHT_VACUUM * (roundTrip * 1E-9)) / 2;
        return d;
    }

    /**
     * This assumes the timestamps have microsecond precision
     */
    public double fromMicroTime(long aSent, long bReceived, long bSent, long aReceived) {
        long roundTrip = (aReceived - aSent) - (bSent - bReceived);
        double d = (Const.SPEED_OF_LIGHT_VACUUM * (roundTrip * 1E-6)) / 2;
        return d;
    }
}
