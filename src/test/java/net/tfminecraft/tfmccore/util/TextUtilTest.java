package net.tfminecraft.tfmccore.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class TextUtilTest {

    @Test
    void expandsUppercaseHexCodes() {
        assertEquals("\u00A7x\u00A7F\u00A7F\u00A70\u00A70\u00A70\u00A70Hello", TextUtil.color("&#FF0000Hello"));
    }

    @Test
    void expandsLowercaseHexCodes() {
        assertEquals("\u00A7x\u00A7f\u00A7f\u00A70\u00A70\u00A7a\u00A7a", TextUtil.color("&#ff00aa"));
    }

    @Test
    void translatesLegacyCodes() {
        assertEquals("\u00A7aGreen", TextUtil.color("&aGreen"));
    }

    @Test
    void translatesMixedHexAndLegacyCodes() {
        assertEquals("\u00A7x\u00A7F\u00A7F\u00A70\u00A70\u00A70\u00A70Red \u00A7aGreen \u00A7x\u00A70\u00A70\u00A70\u00A70\u00A7F\u00A7FBlue",
                TextUtil.color("&#FF0000Red &aGreen &#0000FFBlue"));
    }

    @Test
    void nullInputReturnsNull() {
        assertNull(TextUtil.color(null));
    }

    @Test
    void emptyInputReturnsEmptyString() {
        assertEquals("", TextUtil.color(""));
    }

    @Test
    void leavesTruncatedHexSequenceAlone() {
        // "&#12" is not a full hex colour, so only the trailing legacy code is translated.
        assertEquals("&#12 \u00A7aTest", TextUtil.color("&#12 &aTest"));
    }

    @Test
    void leavesNonHexDigitsAlone() {
        assertEquals("&#GGGGGG", TextUtil.color("&#GGGGGG"));
    }
}
