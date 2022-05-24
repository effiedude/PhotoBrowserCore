package com.townspriter.android.photobrowser.core;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    @Test
    public void useAppContext()
    {
        Context appContext=InstrumentationRegistry.getTargetContext();
        assertEquals("com.townspriter.android.photobrowser.core",appContext.getPackageName());
    }
}
