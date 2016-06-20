package com.moka.signals;

public interface Receiver
{
    void onSignal(String signal, Object value);
}
