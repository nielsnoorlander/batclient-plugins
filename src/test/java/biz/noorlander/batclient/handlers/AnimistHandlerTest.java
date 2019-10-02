package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.model.AnimalSoul;
import com.mythicscape.batclient.interfaces.ClientGUI;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

class AnimistHandlerTest {
    @Mock
    ClientGUI fakeGUI;

    @Test
    void getSelectSoulMatcher() {
        AnimistHandler underTest = new AnimistHandler(fakeGUI);
        Matcher testMatcher = underTest.getSelectSoulMatcher("You select Niliz, the weak pig.");
        assertTrue(testMatcher.find());
        assertEquals("weak", testMatcher.group(1));
        assertEquals("str, con", underTest.getAnimistSoul(testMatcher.group(2)).toString());

        testMatcher = underTest.getSelectSoulMatcher("You select Loose, the awesome griffon.");
        assertTrue(testMatcher.find());
        assertEquals("awesome", testMatcher.group(1));
        assertEquals("str, dex, con, int, wis", underTest.getAnimistSoul(testMatcher.group(2)).toString());
    }

    @Test
    void getSoulListMatcher() {
        AnimistHandler underTest = new AnimistHandler(fakeGUI);
        Matcher testMatcher = underTest.getSoulListMatcher("14     Ightelm                  excellent        badger");
        assertTrue(testMatcher.find());
        assertEquals("excellent", testMatcher.group(3));
        assertEquals("badger", testMatcher.group(4));
        testMatcher = underTest.getSoulListMatcher("4   -> Vesach                   average        goat");
        assertTrue(testMatcher.find());
    }

    @Test
    void getSoulReportMatcher() {
        AnimistHandler underTest = new AnimistHandler(fakeGUI);
        Matcher testMatcher = underTest.getSoulReportMatcher("Total of 20 soul shown. Maximum you can control is 24.");
        assertTrue(testMatcher.find());
        assertEquals(20, Integer.parseInt(testMatcher.group(1)));
        assertEquals(24, Integer.parseInt(testMatcher.group(2)));
    }

    @Test
    void getSoulListHeaderMatcher() {
        AnimistHandler underTest = new AnimistHandler(fakeGUI);
        Matcher testMatcher = underTest.getSoulListHeaderMatcher("Id     Name                     Power          Type");
        assertTrue(testMatcher.find());
    }

    @Test
    void getAnimalSoul() {
        AnimistHandler underTest = new AnimistHandler(fakeGUI);
        AnimalSoul goat = underTest.getAnimistSoul("goat");
        assertEquals("str, wis", goat.toString());
    }
}