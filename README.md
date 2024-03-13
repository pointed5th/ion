## ion

`ion` is a dynamically typed interpreted language for developing healthcare systems.

### Grammar

expression  ->  literal | unary | binary | grouping ;
literal     ->  NUMBER | STRING | "true" | "false" | "nil" ;
grouping    ->  "(" expression ")" ;
unary       ->  ( "-" | "!" ) expression ;
binary      ->  expression operator expression ;
operator    ->  "==" | "!=" | "<" | "<=" | ">" | ">="
                | "+" | "-" | "*" | "/" ;


### Examples

#### Function

`function <identifier>([...arg]) { ?? }`

```js
function square(int num) { return num * num };
```