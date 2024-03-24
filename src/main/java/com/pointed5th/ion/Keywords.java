package com.pointed5th.ion;

import java.util.HashMap;
import java.util.Map;

public  class Keywords {
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("class", TokenType.CLASS);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("function", TokenType.FUNCTION);
        keywords.put("return", TokenType.RETURN);
        keywords.put("var", TokenType.VAR);
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("while", TokenType.WHILE);
        keywords.put("nil", TokenType.NIL);
        keywords.put("print", TokenType.PRINT);

        // FHIR-specific keywords
        keywords.put("Patient", TokenType.RESOURCE);
    }

    public static TokenType get(String key) {
        return keywords.get(key);
    }
}

