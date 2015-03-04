package com.moka.components;

import com.moka.core.Time;
import com.moka.core.Timer;
import com.moka.physics.Collision;

public class Gravity extends Component {
    public static final float GRAVITY = -300f;
    Timer timer;

    @Override
    public void onCreate() {
        timer = Time.newTimer();
    }

    @Override
    public void onUpdate() {
        getTransform().move(0, (float) (GRAVITY * timer.getElapsed() * Time.getDelta()));
    }

    @Override
    public void onCollide(Collision collision) {
        // Grounded
        if (collision.getEntity().getTransform().getLayer() == 0)
            timer.reset();
    }
}
