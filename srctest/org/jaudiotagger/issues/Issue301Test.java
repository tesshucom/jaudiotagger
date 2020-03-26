package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

import java.io.File;

/**
 * Testing shrinking of padding data
 */
public class Issue301Test extends AbstractTestCase
{
    public void testWriteLessDataAndShrink() throws Exception
    {
        File orig = new File("testdata", "test47.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test47.mp3",new File("testStripPadding.mp3"));
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            assertEquals(161,((MP3AudioHeader)af.getAudioHeader()).getMp3StartByte());
            assertEquals(113426,testFile.length());

            //Shorten data but dont request to shrink so same size
            TagOptionSingleton.getInstance().setId3v2PaddingWillShorten(false);
            af.getTag().setField(FieldKey.ALBUM,"Shorter");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            assertEquals(161,((MP3AudioHeader)af.getAudioHeader()).getMp3StartByte());
            assertEquals(113426,testFile.length());

            //Now shorter and request to shrunk so removes all padding
            TagOptionSingleton.getInstance().setId3v2PaddingWillShorten(true);
            af.getTag().setField(FieldKey.ALBUM,"Shorter");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            assertEquals(127,((MP3AudioHeader)af.getAudioHeader()).getMp3StartByte());
            assertEquals(113392,testFile.length());

            //Now a bit longer because more data needed but request to shorten so no spare padding added
            af.getTag().setField(FieldKey.ALBUM,"SlightlyLonger");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            assertEquals(134,((MP3AudioHeader)af.getAudioHeader()).getMp3StartByte());
            assertEquals(113399,testFile.length());

            //Now a bit longer and because have to rewrite audio data anyway we add some spare padding
            TagOptionSingleton.getInstance().setId3v2PaddingWillShorten(false);
            af.getTag().setField(FieldKey.ALBUM,"SoSlightlyLonger");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            assertEquals(236,((MP3AudioHeader)af.getAudioHeader()).getMp3StartByte());
            assertEquals(113501,testFile.length());

            //Nowshorter and set reemove padding
            TagOptionSingleton.getInstance().setId3v2PaddingWillShorten(true);
            af.getTag().setField(FieldKey.ALBUM,"SlightlyLonger");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            assertEquals(134,((MP3AudioHeader)af.getAudioHeader()).getMp3StartByte());
            assertEquals(113399,testFile.length());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }


}
