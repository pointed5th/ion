package com.pointed5th.ion;

import java.beans.Encoder;
import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Environment enclosing;
    private final Map<String, Object> map = new HashMap<>();

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        enclosing = enclosing;
    }
    void define(String name, Object value) {
        // here we are not checking if the already exists.
        map.put(name, value);
    }

    Object get(Token token) {
        if (!exists(token)) throw new RuntimeError(token, "Undefined variable '" + token.lexeme + "'.");
        return map.get(token.lexeme);
    }

    void assign(Token token, Object value) {
        if (!exists(token)) throw new RuntimeError(token, "Undefined variable '" + token.lexeme + "'.");
        map.put(token.lexeme, value);
    }

    boolean exists(Token token) {
        return map.containsKey(token.lexeme);
    }
}
