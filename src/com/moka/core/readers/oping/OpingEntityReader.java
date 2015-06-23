package com.moka.core.readers.oping;

import com.moka.core.contexts.Context;
import com.moka.core.entity.Entity;
import com.moka.core.readers.EntityReader;
import com.shelodev.oping.OpingParser;

public class OpingEntityReader extends EntityReader
{
    public OpingEntityReader(Context context)
    {
        super(context);
    }

    @Override
    public Entity read(String filePath, String entityName)
    {
        OpingParser parser = new OpingParser();
        return null;
    }

    @Override
    protected char getExpressionChar()
    {
        return 0;
    }

    @Override
    protected char getReferenceChar()
    {
        return 0;
    }
}
