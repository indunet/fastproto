package org.indunet.fastproto.ros2;

abstract class Ros2FastProtoTestSupport {
    protected double[] covariance(double base) {
        double[] values = new double[9];
        for (int i = 0; i < values.length; i++) {
            values[i] = base * (i + 1);
        }

        return values;
    }

    protected double[] fixedCovariance(double base) {
        double[] values = new double[36];
        for (int i = 0; i < values.length; i++) {
            values[i] = base * (i + 1);
        }

        return values;
    }
}
