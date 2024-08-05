package org.mkproductions.jnn.entity;


public class Mapper {

    public static double mapRangeToRange(double value, double fromMin, double fromMax, double toMin, double toMax) {
        return toMin + (value - fromMin) * (toMax - toMin) / (fromMax - fromMin);
    }

    public static double mapPredictionToRange(double prediction, ActivationFunction activationFunction, int fromValue, int toValue) {
        if (activationFunction.name().equals(ActivationFunction.SIGMOID.name()))
            return prediction * toValue;
        if (activationFunction.name().equals(ActivationFunction.TAN_H.name()))
            return mapRangeToRange(prediction, -1, 1, fromValue, toValue);
        if (activationFunction.name().equals(ActivationFunction.RE_LU.name()))
            return (prediction < 0) ? 0 : toValue;
        return 0;
    }
}

