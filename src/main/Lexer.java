package main;


import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by John on 15/9/8.
 */
public class Lexer {
//    public static String regexComment = "//.*";
//    public static String regexIndentifier = "[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\\\|\\\\||\\p{Punct}";
//    public static String regexNumber = "[0-9]+";
//    public static String regexString = "\\\"(\\\\\\\\\\\"|\\\\\\\\\\\\\\\\|\\\\\\\\n|[^\\\"])*\\\"";
    public static String regexPat =
                 "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
                + "|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
    private Pattern pattern = Pattern.compile(regexPat);
    private ArrayList<Token> queue = new ArrayList<>();
    private boolean hasMore;
    private LineNumberReader lineReader;

    public Lexer(Reader reader){
        hasMore = true;
        lineReader = new LineNumberReader(reader);
    }

    public Token read() throws ParseException{
        if (fillQueue(0)){
            return queue.remove(0);
        } else {
            return Token.EOF;
        }
    }
    public Token peek(int i) throws ParseException {
        if (fillQueue(i)) {
            return queue.get(i);
        } else {
            return Token.EOF;
        }
    }
    private boolean fillQueue(int i) throws ParseException{
        while (i >= queue.size()){
            if (hasMore){
                readLine();
            } else {
                return false;
            }
        }
        return true;
    }
    protected void readLine() throws ParseException{
        String line;
        try {
            line = lineReader.readLine();
//            System.out.println(line);
        } catch (IOException e){
            throw new ParseException(e);
        }
        if (line == null) {
            hasMore = false;
            return;
        }
        int lineNo = lineReader.getLineNumber();
//        System.out.println(lineNo);
        Matcher matcher = pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int pos = 0;
        int endPos = line.length();
        while (pos < endPos) {
            matcher.region(pos, endPos);
            if (matcher.lookingAt()) {
//                System.out.println(matcher.group(1));
                addToken(lineNo, matcher);
                pos = matcher.end();
            } else {
                throw new ParseException("bad token at line" + line);
            }
        }
        queue.add(new IdToken(lineNo, Token.EOL));
    }

    private void addToken(int lineNo, Matcher matcher) {
        String message = matcher.group(1);
//        System.out.println(message);
        if (message != null){
            if (matcher.group(2) == null){
                Token token;
                if (matcher.group(3) != null){
                    token = new NumToken(lineNo, Integer.parseInt(message));
                } else if (matcher.group(4) != null) {
                    token = new StrToken(lineNo, toStringLiteral(message));
                } else {
                    token = new IdToken(lineNo, message);
                }
                queue.add(token);
            }
        }
    }
    protected String toStringLiteral(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length() - 1;
        for (int i = 1; i < len; i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < len) {
                int c2 = s.charAt(i + 1);
                if (c2 == '"' || c2 == '\\')
                    c = s.charAt(++i);
                else if (c2 == 'n') {
                    ++i;
                    c = '\n';
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    protected static class NumToken extends Token {
        private int value;

        protected NumToken(int line, int v) {
            super(line);
            value = v;
        }
        public boolean isNumber() { return true; }
        public String getText() { return Integer.toString(value); }
        public int getNumber() { return value; }
    }

    protected static class IdToken extends Token {
        private String text;
        protected IdToken(int line, String id) {
            super(line);
            text = id;
        }
        public boolean isIdentifier() { return true; }
        public String getText() { return text; }
    }

    protected static class StrToken extends Token {
        private String literal;
        StrToken(int line, String str) {
            super(line);
            literal = str;
        }
        public boolean isString() { return true; }
        public String getText() { return literal; }
    }
}
