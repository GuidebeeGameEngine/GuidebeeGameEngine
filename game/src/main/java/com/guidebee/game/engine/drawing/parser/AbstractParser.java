/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
//--------------------------------- PACKAGE ------------------------------------
package com.guidebee.game.engine.drawing.parser;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * <code>AbstractParser</code> is the base class for parsers found
 * in this package. <br />
 * All parsers work on a <code>String</code> and the <code>AbstractParser</code>
 * keeps a reference to that string along with the current position
 * (@see #currentPos) and current character (@see current). <br />
 * The key methods for this class are <code>read</code> which reads the next
 * character in the parsed string, <code>setString</code> which sets the string
 * to be parsed, and the utility methods <code>skipCommaSpaces</code>,
 * <code>skipSpaces</code> and <code>skipSpacesCommaSpaces</code> which can
 * be used by descendants to skip common separators.
 * <br />
 * For an implementation example, see {@link TransformListParser}.
 *
 * @author James Shen.
 */
public abstract class AbstractParser {

    /**
     * The current position in the string
     */
    protected int currentPos;
    /**
     * The String being parsed
     */
    protected String s;
    /**
     * The current character being parsed
     * This is accessible by sub-classes
     */
    protected int current;


    /**
     * @return the next character. Returns -1 when the
     * end of the String has been reached.
     */
    protected final int read() {
        if (currentPos < s.length()) {
            return s.charAt(currentPos++);
        }
        return -1;
    }


    /**
     * Sets this parser's String. This also resets the
     * current position to 0
     *
     * @param str the string this parser should parse. Should
     *            not be null.
     */
    protected final void setString(final String str) {
        if (str == null) {
            throw new IllegalArgumentException();
        }

        this.s = str;
        this.currentPos = 0;
        this.current = -1;
    }


    /**
     * Skips the whitespaces in the current reader.
     */
    protected final void skipSpaces() {
        for (; ; ) {
            switch (current) {
                default:
                    return;
                case 0x20:
                case 0x09:
                case 0x0D:
                case 0x0A:
            }
            current = read();
        }
    }


    /**
     * Skips the whitespaces and an optional comma.
     */
    protected final void skipCommaSpaces() {
        skipSepSpaces(',');
    }


    /**
     * Skips the whitespaces and an optional comma.
     *
     * @param sep seperator to skip in addition to spaces.
     */
    protected final void skipSepSpaces(final char sep) {
        wsp1:
        for (; ; ) {
            switch (current) {
                default:
                    break wsp1;
                case 0x20:
                case 0x9:
                case 0xD:
                case 0xA:
            }
            current = read();
        }
        if (current == sep) {
            wsp2:
            for (; ; ) {
                switch (current = read()) {
                    default:
                        break wsp2;
                    case 0x20:
                    case 0x9:
                    case 0xD:
                    case 0xA:
                }
            }
        }
    }


    /**
     * Skips wsp*,wsp* and throws an IllegalArgumentException
     * if no comma is found.
     */
    protected final void skipSpacesCommaSpaces() {
        skipSpaces();

        if (current != ',') {
            throw new IllegalArgumentException();
        }

        current = read();
        skipSpaces();
    }


    /**
     * Tests if the current substring (i.e. the substring beginning at the
     * current position) starts with the specified prefix.  If the current
     * substring starts with the specified prefix, the current character will
     * be updated to point to the character immediately following the last
     * character in the prefix; otherwise, the <code>currentPos</code> will
     * not be affected.  For example, if the string being parsed is
     * "timingAttr", and the current character is 'A':
     * <pre>
     *   currentStartsWith("Att") returns true, current == 'r'
     *   currentStartsWith("Attr") returns true, current == -1
     *   currentStartsWith("Attx") returns false, current == 'A'
     * </pre>
     *
     * @param str the prefix to be tested
     * @return <code>true</code> if the current substring starts with the
     * specified prefix.  The result is <code>false</code> if
     * <code>currentPos</code> is non-positive, or if the current substring
     * does not start with the specified prefix.
     */
    protected final boolean currentStartsWith(final String str) {
        if (currentPos <= 0) {
            return false;
        }
        if (s.startsWith(str, currentPos - 1)) {
            currentPos += str.length() - 1;
            current = read();
            return true;
        }
        return false;
    }

}

