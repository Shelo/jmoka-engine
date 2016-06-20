package example.signals.components.demo;

import com.moka.scene.entity.Component;

public class DemoEmitter extends Component
{
    public static final String SIGNAL_SAMPLE = "mySampleSignal";

    @Override
    public void onCreate()
    {

    }

    @Override
    public void onUpdate()
    {
        emit(SIGNAL_SAMPLE, 42);
    }
}
