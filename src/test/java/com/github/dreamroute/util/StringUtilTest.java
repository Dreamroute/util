package com.github.dreamroute.util;

import org.junit.jupiter.api.Test;

import static com.github.dreamroute.util.StringUtil.getValiCode4;
import static com.github.dreamroute.util.StringUtil.getValiCode6;
import static com.github.dreamroute.util.StringUtil.humpToUnderline;
import static com.github.dreamroute.util.StringUtil.isEmpty;
import static com.github.dreamroute.util.StringUtil.isNotEmpty;
import static com.github.dreamroute.util.StringUtil.underlineToHump;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilTest {

    @Test
    void humpToUnderlineTest() {
        String userName = humpToUnderline("userName");
        assertEquals("user_name", userName);
    }

    @Test
    void underlineToHumpTest() {
        String userName = underlineToHump("user_name");
        assertEquals("userName", userName);
    }

    @Test
    void getValiCode4Test() {
        String valiCode4 = getValiCode4();
        assertEquals(4, valiCode4.length());
    }

    @Test
    void getValiCode6Test() {
        String valiCode6 = getValiCode6();
        assertEquals(6, valiCode6.length());
    }

    @Test
    void isEmptyTest() {
        assertFalse(isEmpty("w"));
        assertTrue(isEmpty(null));
        assertTrue(isEmpty(""));
        assertTrue(isEmpty(" "));
    }

    @Test
    void isNotEmptyTest() {
        assertTrue(isNotEmpty("w"));
        assertFalse(isNotEmpty(null));
        assertFalse(isNotEmpty(""));
        assertFalse(isNotEmpty(" "));
    }
}
