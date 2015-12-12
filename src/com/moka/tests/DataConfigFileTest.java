package com.moka.tests;

import com.moka.utils.ConfigDataFile;
import com.shelodev.oping2.structure.Leaf;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class DataConfigFileTest
{
    @Test
    public void testSave()
    {
        ConfigDataFile file = new ConfigDataFile("testfile.oping");
        file.setName("Branch");
        file.addLeaf(new Leaf("Leaf1", "0", "1"));
        file.addLeaf(new Leaf("Leaf2", "1", "2"));
        file.save();

        File saved = file.getFileHandle().getFile();
        assertThat(saved.exists(), is(true));
        saved.delete();
    }

    @Test
    public void testSaveAndLoad()
    {
        ConfigDataFile file = new ConfigDataFile("testfile.oping");
        file.setName("Branch");
        file.addLeaf(new Leaf("Leaf1", "0", "1"));
        file.addLeaf(new Leaf("Leaf2", "1", "2"));
        file.save();

        ConfigDataFile load = new ConfigDataFile("testfile.oping");
        assertThat(load.getName(), equalTo("Branch"));
        assertThat(load.getLeaf("Leaf1"), notNullValue());
        assertThat(load.getLeaf("Leaf2"), notNullValue());

        assertThat(load.getLeaf("Leaf1").getValues().size(), equalTo(2));
        assertThat(load.getLeaf("Leaf1").getValues().get(0), equalTo("0"));
        assertThat(load.getLeaf("Leaf1").getValues().get(1), equalTo("1"));

        assertThat(load.getLeaf("Leaf2").getValues().size(), equalTo(2));
        assertThat(load.getLeaf("Leaf2").getValues().get(0), equalTo("1"));
        assertThat(load.getLeaf("Leaf2").getValues().get(1), equalTo("2"));

        file.getFileHandle().getFile().delete();
    }
}
