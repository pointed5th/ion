package com.pointed5th.ion;

import java.util.ArrayList;
import java.util.List;

import static com.pointed5th.ion.TokenType.*;


public class Lexer {
    private final String source;
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private final List<Token> tokens = new ArrayList<>();

    Lexer(String source) {
        this.source = source;
    }

    List<Token> lex() {
        while (!EOF()) {
            start = current;
            scan();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scan() {
        char current = seek();

        switch (current) {
            case '(' -> consume(LEFT_PAREN);
            case ')' -> consume(RIGHT_PAREN);
            case '{' -> consume(LEFT_BRACE);
            case '}' -> consume(RIGHT_BRACE);
            case ',' -> consume(COMMA);
            case '.' -> consume(DOT);
            case '-' -> consume(MINUS);
            case '+' -> consume(PLUS);
            case ';' -> consume(SEMICOLON);
            case '*' -> consume(STAR);
            case '!' -> consume(match('=') ? BANG_EQUAL : BANG);
            case '=' -> consume(match('=') ? EQUAL_EQUAL : EQUAL);
            case '<' -> consume(match('=') ? LESS_EQUAL : LESS);
            case '>' -> consume(match('=') ? GREATER_EQUAL : GREATER);
            case '/' -> {
                if (match('/')) {
                    // consume comment
                    while (peek() != '\n' && !EOF()) seek();
                } else {
                    consume(SLASH);
                }
            }
            // ignore whitespace
            case ' ', '\r', '\t' -> {}
            case '\n' -> line++;
            case '"' -> string();
            case 'o' -> {
                if (match('r')) {
                    consume(OR);
                }
            }
            default -> {
                if (isDigit(current)) {
                    number();
                } else if (isAlpha(current)){
                    identifier();
                } else {
                    Ion.error(line, "Error: Unexpected character " + current);
                }
            }
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) seek();
        String text = source.substring(start, current);
        TokenType type = Keywords.get(text);
        if (type == null) type = IDENTIFIER;
        consume(type);
    }

    private boolean isAlpha(char c) {
        return Character.isAlphabetic(c) || c == '_';
    }

    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private boolean isAlphaNumeric(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    private void number() {
        while (isDigit(peek())) seek();

        if (peek() == '.' && isDigit(peekNext())) {
            seek();
            while (isDigit(peek())) seek();
        }
        consume(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void string() {
        while (peek() != '"' && !EOF()) {
            if (peek() == '\n') line++;
            seek();
        }

        if (EOF()) {
            Ion.error(line, "unterminated string, missing '\"'");
            return;
        }

        seek();

        String value = source.substring(start + 1, current - 1);

        if (value.length() > 255) {
            Ion.error(line, "string length exceeds 255 characters");
        }

        consume(STRING, value);
    }

    private char peek() {
        if (EOF()) return '\0';
        return source.charAt(current);
    }

   private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private char seek() {
        return source.charAt(current++);
    }

    private boolean match(char expected) {
        if (EOF()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private void consume(TokenType type) {
        consume(type, null);
    }

    private void consume(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean EOF() {
        return current >= source.length();
    }
}
