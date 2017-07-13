package org.jaudiotagger.audio;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldKey;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Able to write language ensures writes it as iso code for mp3s
 */
public class AudioFileWriteAsTest extends AbstractTestCase {

    public static final String EXPECTED_EXTENSION = ".mp3";
    public static final String LANGUAGE = "English";
    private static final String DESTINATION_FILE_NAME = "writeastest";
    private AudioFile af;
    private File sourceFile;

    @Before
    public void setUp() throws Exception {
        File orig = new File("testdata", "01.mp3");
        try {
            sourceFile = copyAudioToTmp(orig.getName());
            af = AudioFileIO.read(sourceFile);
        } catch (Throwable e) {
            throw new RuntimeException("Can't setUp test.", e);
        }
    }

    @Test
    public void testWriteAs() throws Exception
    {
        af.getTagOrCreateAndSetDefault().setField(FieldKey.LANGUAGE, LANGUAGE);
        af.commit();

        final String parent = sourceFile.getParent();
        File destinationNoExtension = new File(parent, DESTINATION_FILE_NAME);
        AudioFileIO.writeAs(af, destinationNoExtension.getPath());

        assertEquals(destinationNoExtension + EXPECTED_EXTENSION, af.getFile().getPath());
        assertEquals(LANGUAGE, af.getTag().getFirst(FieldKey.LANGUAGE));
    }

    @Test
    public void testWriteAsWithNull() throws Exception
    {
        try {
            AudioFileIO.writeAs(af, null);
        } catch (CannotWriteException e) {
            // expected
            return;
        }
        fail("Didn't get expected exception " + CannotWriteException.class);
    }
}