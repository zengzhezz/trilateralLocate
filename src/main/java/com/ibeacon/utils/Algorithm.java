package com.ibeacon.utils;

import org.junit.Test;

/**
 * 算法工具类
 * @author zz
 * @version 1.0 2017年4月28日
 */
public class Algorithm {
    static int STAND_RSSI=49;
    /**
     * 初始化参数,(d0, pd0, n, s)

     private static final double[][] param = {
     {1, -47, 1.05, -1.15},
     {5, -55, 1.06, -0.56},
     {10, -65, 3.56, -0.25},
     {15, -70, 2.96, -0.58},
     {20, -75, 5.78, -2.40},
     };
     */
    /**
     * 根据rssi算出实际距离
     * @param rrsi
     * @return
     */
    public static double calDistanceWithRssi(int rssi){
        // 是否使用计算方法标志，flag=true需要计算，否则不需要计算
        boolean flag = true;
        double result = 0;
        double index = 0;
        if(rssi <=30){
            result = 0;
            flag = false;
        }else if(rssi <= 61){
            index = 1.7;

        }else{
            index = 2.1;
        }

        if(flag){
            // 有rssi计算实际距离的算法公式
            int power = rssi-STAND_RSSI;
            double a = 10*index;
            double b = power/a;
            double pow_x =Math.pow(10, b);
            result = pow_x;

        }
        return result;
    }

    @Test
    public void test(){
        double s = calDistanceWithRssi(-65);
        System.out.println(s);
    }

    /**
     * 内部类，定义算法的各个参数，包括d0,pd0,n,s
     * @author zz
     * @version 1.0 2017年4月28日
     */
    private class Model{
        double d0;
        double pd0;
        double n;
        double s;
        public Model() {
            super();
        }
        public Model(double d0, double pd0, double n, double s) {
            super();
            this.d0 = d0;
            this.pd0 = pd0;
            this.n = n;
            this.s = s;
        }
        public double getD0() {
            return d0;
        }
        public void setD0(double d0) {
            this.d0 = d0;
        }
        public double getPd0() {
            return pd0;
        }
        public void setPd0(double pd0) {
            this.pd0 = pd0;
        }
        public double getN() {
            return n;
        }
        public void setN(double n) {
            this.n = n;
        }
        public double getS() {
            return s;
        }
        public void setS(double s) {
            this.s = s;
        }
    }

}
