package main;

import java.io.IOException;

/**
 * Created by John on 15/9/8.
 */
public class ParseException extends Exception{
    public ParseException(Token token){
        this("",token);
    }
    public ParseException(String message, Token token){
        super("syntax error around" + location(token) + ". " + message);
    }
    private static String location(Token token){
        if (token == Token.EOF){
            return "the last line";
        } else {
            return "\"" + token.getText() + "\" at line " + token.getLineNumber();
        }
    }
    public ParseException(IOException e){
        super(e);
    }
    public ParseException(String message){
        super(message);
    }
}
