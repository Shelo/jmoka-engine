package com.moka.core.readers;

import com.moka.core.Prefab;

public abstract class PrefabReader
{
    public abstract Prefab newPrefab(String filePath);
}
