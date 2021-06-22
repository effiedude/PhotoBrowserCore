package com.townspriter.android.template;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    private static final String PACKAGExNAME="com.townspriter.android.template";
    
    @Test
    public void useAppContext()
    {
        Context appContext=InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals(ExampleInstrumentedTest.PACKAGExNAME,appContext.getPackageName());
    }
}