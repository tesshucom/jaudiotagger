package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagOptionSingleton;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Test, useful for just reading file info
 */
public class ReadFileTest extends AbstractTestCase
{
    public void testReadFile() throws Exception
    {
        final TagOptionSingleton tagOptions = TagOptionSingleton.getInstance();
        tagOptions.setToDefault();

        File orig = new File("testdata", "test800.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test800.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            for (FieldKey fieldKey : FieldKey.values())
            {
                if (af.getTag().hasField(fieldKey))
                {
                    System.out.println("Checking FieldKey:"+fieldKey);
                    List<String> values = af.getTag().getAll(fieldKey);
                    for (String val : values)
                    {
                        System.out.println("Reading Value:"+fieldKey+":"+val);
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }
}
