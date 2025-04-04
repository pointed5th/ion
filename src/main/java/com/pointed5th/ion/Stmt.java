package com.pointed5th.ion;

import java.util.List;

abstract class Stmt {
 interface Visitor<R> {
    R visitExpressionStmt(Expression stmt);
    R visitVarStmt(Var stmt);
    R visitPrintStmt(Print stmt);
 }
    static class Expression extends Stmt {
    Expression(Expr expression) {
     this.expression = expression;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
     return visitor.visitExpressionStmt(this);
    }
    final Expr expression;
  }
    static class Var extends Stmt {
    Var(Token name, Expr initializer) {
     this.name = name;
     this.initializer = initializer;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
     return visitor.visitVarStmt(this);
    }
    final Token name;
    final Expr initializer;
  }
    static class Print extends Stmt {
    Print(Expr expression) {
     this.expression = expression;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
     return visitor.visitPrintStmt(this);
    }
    final Expr expression;
  }

 abstract <R> R accept(Visitor<R> visitor);
}
