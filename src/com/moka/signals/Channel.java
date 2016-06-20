package com.moka.signals;

import com.moka.utils.JMokaException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Channel
{
    private HashMap<String, List<Receiver>> registry = new HashMap<>();

    public void registerReceiver(String signal, Receiver receiver)
    {
        if (registry.containsKey(signal)) {
            List<Receiver> receivers = registry.get(signal);
            receivers.add(receiver);
        } else {
            List<Receiver> receivers = new ArrayList<>();
            receivers.add(receiver);
            registry.put(signal, receivers);
        }
    }

    public void broadcast(String signal, Object value)
    {
        if (registry.containsKey(signal)) {
            List<Receiver> receivers = registry.get(signal);

            for (Receiver receiver : receivers) {
                receiver.onSignal(signal, value);
            }
        } else {
            throw new JMokaException("No such signal: " + signal);
        }
    }
}
