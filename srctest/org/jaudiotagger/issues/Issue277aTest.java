package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import java.io.File;

/**
 * Test Writing to new urls with common interface
 */
public class Issue277aTest extends AbstractTestCase
{
    /**
     * Test Write Mp4
     */
    public void testWriteMp4()
    {
        ID3v24Tag v24tag = null;
        ID3v22Tag v22tag = null;
        File orig = new File("testdata", "test277.m4a");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test277.m4a");

            new Mp4AtomTree(testFile).printAtomTree();

            AudioFile af = AudioFileIO.read(testFile);
            //af.getTag().setField(FieldKey.ALBUM,"012345678901");
            //af.commit();

            //af = AudioFileIO.read(testFile);
            //new Mp4AtomTree(testFile).printAtomTree();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        //assertNull(exceptionCaught);
    }
}
