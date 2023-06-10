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

import tech.deplant.osiris.node.io.cron.DayOfWeekField;
import tech.deplant.osiris.node.io.cron.Token;
import tech.deplant.osiris.node.io.cron.Tokens;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokensTest {
    private Tokens tokens;

    @Test
    public void empty() {
        tokenize("");
        assertFalse(this.tokens.hasNext());
        assertEndOfInput();
    }

    @Test
    public void end() {
        tokenize("1");
        assertNextIsNumber(1);
        assertFalse(this.tokens.hasNext());
        assertEndOfInput();
    }

    @Test
    public void offset() {
        tokenize("5,6");
        this.tokens.offset(5);
        assertNextIsNumber(0);
        assertNextIs(Token.VALUE_SEPARATOR);
        assertNextIsNumber(1);
        assertEndOfInput();
    }

    @Test
    public void resetClearsOffset() {
        tokenize("2,2");
        this.tokens.offset(1);
        assertNextIsNumber(1);
        assertNextIs(Token.VALUE_SEPARATOR);
        this.tokens.reset();
        assertNextIsNumber(2);
    }

    @Test
    public void resetClearsKeywords() {
        tokenize("FRI,FRI");
        this.tokens.keywords(DayOfWeekField.Builder.KEYWORDS);
        assertNextIsNumber(5);
        assertNextIs(Token.VALUE_SEPARATOR);
        this.tokens.reset();
        try {
            this.tokens.next();
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Bad keyword 'FRI' at position 4 in string: FRI,FRI", e.getMessage());
        }
    }

    @Test
    public void matchOne() {
        tokenize("?");
        assertNextIs(Token.MATCH_ONE);
        assertEndOfInput();
    }

    @Test
    public void matchAll() {
        tokenize("*");
        assertNextIs(Token.MATCH_ALL);
        assertEndOfInput();
    }

    @Test
    public void skip() {
        tokenize("/");
        assertNextIs(Token.SKIP);
        assertEndOfInput();
    }

    @Test
    public void range() {
        tokenize("-");
        assertNextIs(Token.RANGE);
        assertEndOfInput();
    }

    @Test
    public void last() {
        tokenize("1L");
        assertNextIsNumber(1);
        assertNextIs(Token.LAST);
        assertEndOfInput();
    }

    @Test
    public void lastAlone() {
        tokenize("L");
        assertNextIs(Token.LAST);
        assertEndOfInput();
    }

    @Test
    public void weekday() {
        tokenize("1W");
        assertNextIsNumber(1);
        assertNextIs(Token.WEEKDAY);
        assertEndOfInput();
    }

    @Test
    public void nth() {
        tokenize("1#2");
        assertNextIsNumber(1);
        assertNextIs(Token.NTH);
        assertNextIsNumber(2);
        assertEndOfInput();
    }

    @Test
    public void multipleWhitespaceCharacters() {
        tokenize(" \t \t \t \t ");
        assertEquals(Token.FIELD_SEPARATOR, this.tokens.next());
        assertEndOfInput();
    }

    @Test
    public void keywordRange() {
        tokenize("MON-FRI");
        this.tokens.keywords(DayOfWeekField.Builder.KEYWORDS);
        assertEquals(Token.NUMBER, this.tokens.next());
        assertEquals(1, this.tokens.number());
        assertEquals(Token.RANGE, this.tokens.next());
        assertEquals(Token.NUMBER, this.tokens.next());
        assertEquals(5, this.tokens.number());
        assertEndOfInput();
    }

    @Test
    public void badCharacter() {
        tokenize("5%");
        assertEquals(Token.NUMBER, this.tokens.next());
        try {
            this.tokens.next();
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Bad character '%' at position 1 in string: 5%", e.getMessage());
        }
    }

    @Test
    public void badLetter() {
        tokenize("1F");
        assertEquals(Token.NUMBER, this.tokens.next());
        try {
            this.tokens.next();
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Bad character 'F' at position 1 in string: 1F", e.getMessage());
        }
    }

    @Test
    public void badKeywordOfValidLength() {
        tokenize("ABC");
        this.tokens.keywords(DayOfWeekField.Builder.KEYWORDS);
        try {
            this.tokens.next();
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Bad keyword 'ABC' at position 0 in string: ABC", e.getMessage());
        }
    }

    @Test
    public void badKeywordOfInvalidLength() {
        tokenize("AB");
        this.tokens.keywords(DayOfWeekField.Builder.KEYWORDS);
        try {
            this.tokens.next();
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Bad keyword 'AB' at position 0 in string: AB", e.getMessage());
        }
    }

    private void assertEndOfInput() {
        assertNextIs(Token.END_OF_INPUT);
    }

    private void assertNextIsNumber(int expected) {
        assertNextIs(Token.NUMBER);
        assertEquals(expected, this.tokens.number());
    }

    private void assertNextIs(Token expected) {
        assertEquals(expected, this.tokens.next());
    }

    private void tokenize(String s) {
        this.tokens = new Tokens(s);
    }
}
