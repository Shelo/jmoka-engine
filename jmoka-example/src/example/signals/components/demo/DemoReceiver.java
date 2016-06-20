package example.signals.components.demo;

import com.moka.scene.entity.Component;

public class DemoReceiver extends Component
{
    @Override
    public void onCreate()
    {
        registerAsReceiverFor(DemoEmitter.SIGNAL_SAMPLE);
    }

    @Override
    public void onSignal(String signal, Object value)
    {
        switch (signal) {
            case DemoEmitter.SIGNAL_SAMPLE:
                log(String.valueOf(value));
                break;
        }
    }
}
