package com.pointed5th.ion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pointed5th.ion.TokenType.*;


public class Scanner {
    private final String source;
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("function", FUNCTION);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    Scanner(String source) {
        this.source = source;

    }

    List<Token> scan() {
        while (!EOF()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char current = seek();

        switch (current) {
            case '(' -> addToken(LEFT_PAREN);
            case ')' -> addToken(RIGHT_PAREN);
            case '{' -> addToken(LEFT_BRACE);
            case '}' -> addToken(RIGHT_BRACE);
            case ',' -> addToken(COMMA);
            case '.' -> addToken(DOT);
            case '-' -> addToken(MINUS);
            case '+' -> addToken(PLUS);
            case ';' -> addToken(SEMICOLON);
            case '*' -> addToken(STAR);
            case '!' -> addToken(match('=') ? BANG_EQUAL : BANG);
            case '=' -> addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            case '<' -> addToken(match('=') ? LESS_EQUAL : LESS);
            case '>' -> addToken(match('=') ? GREATER_EQUAL : GREATER);
            case '/' -> {
                if (match('/')) {
                    while (peek() != '\n' && !EOF()) seek();
                } else {
                    addToken(SLASH);
                }
            }
            // Ignore whitespace
            case ' ', '\r', '\t' -> {}
            case '\n' -> line++;
            case '"' -> string();
            case 'o' -> {
                if (match('r')) {
                    addToken(OR);
                }
            }
            default -> {
                if (isDigit(current)) {
                    number();
                } else if (isAlpha(current)){
                    identifier();
                } else {
                    Ion.error(line, "Error: Unexpected character.");
                }
            }
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) seek();
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c == '_');
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        while (isDigit(peek())) seek();

        if (peek() == '.' && isDigit(peek(1))) {
            seek();
            while (isDigit(peek())) seek();
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }



    private void string() {
        while (peek() != '"' && EOF()) {
            if (peek() == '\n') line++;
            seek();
        }

        if (EOF()) {
            Ion.error(line, "Error: unterminated string, missing '\"'");
            return;
        }

        seek();

        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private char peek() {
        if (EOF()) return '\0';
        return source.charAt(current);
    }

    private char peek(int offset) {
        if (current + offset >= source.length()) return '\0';
        return source.charAt(current + offset);
    }

    private boolean match(char expected) {
        if (EOF()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char seek() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean EOF() {
        return current >= source.length();
    }
}
