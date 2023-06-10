/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Anders Wisch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tech.deplant.osiris.node.tst.unit.cron;

import tech.deplant.osiris.node.io.cron.Keywords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class KeywordsTest {
    private Keywords keywords;

    @BeforeEach
    public void before() {
        this.keywords = new Keywords();
    }

    @Test
    public void normalUse() {
        this.keywords.put("AAA", 1);
        this.keywords.put("BBB", 2);
        assertEquals(1, this.keywords.get("AAABBB", 0, 3));
        assertEquals(2, this.keywords.get("AAABBB", 3, 6));
    }

    @Test
    public void getNotPresent() {
        try {
            assertEquals(-1, this.keywords.get("CCC", 0, 3));
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void getWrongAlphabet() {
        try {
            this.keywords.get("aaa", 0, 3);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void getWrongLength() {
        try {
            this.keywords.get("aaa", 0, 1);
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void putEmpty() {
        try {
            this.keywords.put("", 0);
            fail("Expected exception");
        } catch (StringIndexOutOfBoundsException e) {
            assertEquals("Index 0 out of bounds for length 0", e.getMessage());
        }
    }

    @Test
    public void putWrongLength() {
        try {
            this.keywords.put("A", 0);
            fail("Expected exception");
        } catch (StringIndexOutOfBoundsException e) {
            assertEquals("Index 1 out of bounds for length 1", e.getMessage());
        }
    }

    @Test
    public void putWrongAlphabet() {
        try {
            this.keywords.put("a", 0);
            fail("Expected exception");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Index 32 out of bounds for length 26", e.getMessage());
        }
    }
}
