package main;

import AST.ASTree;

/**
 * Created by John on 15/9/8.
 */
public class SandException extends RuntimeException {
    public SandException(String message) {
        super(message);
    }

    public SandException(String message, ASTree tree) {
        super(message + " " + tree.location());
    }
}
