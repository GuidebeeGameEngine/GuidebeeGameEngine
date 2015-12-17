package com.guidebee.game.graphics;

import com.guidebee.drawing.VectorFont;
import com.guidebee.game.files.FileHandle;

import java.io.IOException;


/**
 * True type font.
 */
public class TrueTypeFont extends VectorFont {

    public TrueTypeFont(FileHandle file) throws IOException {
        super(file.read(4096));
    }
}
