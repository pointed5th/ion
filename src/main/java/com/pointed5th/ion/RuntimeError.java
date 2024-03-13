package com.pointed5th.ion;

public class RuntimeError extends RuntimeException {
    final Token token;

    RuntimeError(Token token, String message) {
        super("RuntimeError: " + message + " at line " + token.line);
        this.token = token;
    }
}
