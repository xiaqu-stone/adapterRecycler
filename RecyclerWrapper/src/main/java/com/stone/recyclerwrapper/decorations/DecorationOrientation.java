package com.stone.recyclerwrapper.decorations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created By: sqq
 * Created Time: 17/4/5 下午7:44.
 */
@IntDef({DecorationOrientation.LEFT, DecorationOrientation.TOP, DecorationOrientation.RIGHT, DecorationOrientation.BOTTOM})
@Retention(RetentionPolicy.CLASS)
public @interface DecorationOrientation {
    int LEFT = 0x01;
    int TOP = 0x02;
    int RIGHT = 0x04;
    int BOTTOM = 0x08;
}
