package com.cgvsu.objreader;

import com.cgvsu.GuiController;

public class PathReadException extends Exception {
    public PathReadException() {

        super("Error in file path, pls check it");

        GuiController.exception("Error, wrong file path");
    }

}
