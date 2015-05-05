package com.moka.core.readers;

public interface SceneReader
{
    void read(String filePath);
    EntityReader getEntityReader();
}
