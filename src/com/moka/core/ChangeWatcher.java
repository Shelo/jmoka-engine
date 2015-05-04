package com.moka.core;

import com.moka.utils.JMokaException;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class ChangeWatcher
{
    WatchService watcher;
    WatchKey key;
    Path dir;

    public ChangeWatcher(String path)
    {
        dir = Paths.get(path);

        try
        {
            watcher = FileSystems.getDefault().newWatchService();
            WatchKey key = dir.register(watcher, ENTRY_MODIFY);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't register the file watcher.");
        }
    }

    /**
     * Indicates whenever a change has been made on one of the file in the directory.
     * @return true if something has changed.
     */
    public boolean hasChanges()
    {
        key = watcher.poll();

        if (key == null)
        {
            return false;
        }

        boolean changes = false;
        for (WatchEvent<?> event : key.pollEvents())
        {
            WatchEvent.Kind<?> kind = event.kind();
            changes = kind == OVERFLOW || kind == ENTRY_MODIFY;
        }

        key.reset();
        return changes;
    }
}
