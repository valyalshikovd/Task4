package com.cgvsu.objreader;

import com.cgvsu.GuiController;

public class ObjReaderException extends RuntimeException {
    public ObjReaderException(String errorMessage, int lineInd) {
        super("Error parsing OBJ file on line: " + lineInd + ". " + errorMessage);
        GuiController.exception("Error parsing OBJ file on line: " + lineInd + ". " + errorMessage);

    }
}