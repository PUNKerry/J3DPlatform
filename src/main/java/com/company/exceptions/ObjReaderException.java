package com.company.exceptions;

public class ObjReaderException extends RuntimeException {
    public ObjReaderException(String errorMessage, int lineInd) {
        super("Error parsing OBJ file on line: " + lineInd + ". " + errorMessage);
    }
    public ObjReaderException(String errorMessage) {
        super("Error parsing OBJ file: " + errorMessage);
    }
}
