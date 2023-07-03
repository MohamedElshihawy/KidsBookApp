package com.example.book_v2.ui.Effects;


import android.graphics.drawable.Drawable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;


public class Animation {

    private static Animation animation;
    private Drawable drawable;

    private Animation() {
    }


    public static Animation getInstance() {
        if (animation == null) {
            animation = new Animation();
        }
        return animation;
    }


    public Party startSimpleAnimation() {
        return simpleAnimation();
    }

    public Party startExplodeAnimation() {
        return explode();
    }

    public Party startRainAnimation() {
        return rain();
    }

    public Party startParadeAnimation() {
        return parade();
    }


    private Party simpleAnimation() {

        Shape.DrawableShape drawableShape = new Shape.DrawableShape(drawable, true);

        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);

        return new PartyFactory(emitterConfig)
                .angle(270)
                .spread(90)
                .setSpeedBetween(1f, 5f)
                .timeToLive(2000L)
                .shapes(new Shape.Rectangle(0.2f), drawableShape)
                .sizes(new Size(12, 5f, 0.2f))
                .position(0.0, 0.0, 1.0, 0.0)
                .build();
    }


    private Party explode() {
        Shape.DrawableShape drawableShape = new Shape.DrawableShape(drawable, true);
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        return new PartyFactory(emitterConfig)
                .spread(360)
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(0f, 30f)
                .position(new Position.Relative(0.5, 0.3))
                .build();
    }

    private Party parade() {

        Shape.DrawableShape drawableShape = new Shape.DrawableShape(drawable, true);
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(30);
        //                new PartyFactory(emitterConfig)
//                        .angle(Angle.LEFT + 45)
//                        .spread(Spread.SMALL)
//                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
//                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
//                        .setSpeedBetween(10f, 30f)
//                        .position(new Position.Relative(1.0, 0.5))
//                        .build();

        return new PartyFactory(emitterConfig)
                .angle(Angle.RIGHT - 45)
                .spread(Spread.SMALL)
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(10f, 30f)
                .position(new Position.Relative(0.0, 0.5))
                .build();
    }

    private Party rain() {
        Shape.DrawableShape drawableShape = new Shape.DrawableShape(drawable, true);
        EmitterConfig emitterConfig = new Emitter(6, TimeUnit.SECONDS).perSecond(100);

        return new PartyFactory(emitterConfig)
                .angle(Angle.BOTTOM)
                .spread(Spread.ROUND)
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(0f, 15f)
                .position(new Position.Relative(0.0, 0.0).between(new Position.Relative(1.0, 0.0)))
                .build();

    }


    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

}

