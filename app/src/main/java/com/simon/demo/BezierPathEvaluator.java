package com.simon.demo;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class BezierPathEvaluator implements TypeEvaluator<PointF> {
    //控制点
    private PointF pointflagF1;
    private PointF pointflagF2;

    public BezierPathEvaluator(PointF pointflagF1, PointF pointflagF2) {
        this.pointflagF1 = pointflagF1;
        this.pointflagF2 = pointflagF2;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        return BezierUtils.CalculateBezierPointForCubic(fraction,startValue,pointflagF1,pointflagF2, endValue);
    }

}
