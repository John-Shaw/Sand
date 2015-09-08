package main;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, ParseException {
        Scanner scan = new Scanner(System.in);
        System.out.println("文件路径:");

        String fileName = scan.next().trim();
        System.out.println("打开文件:" + fileName);
        File file = new File(fileName);

        Lexer lexer = new Lexer(new FileReader(file));
        for (Token token; (token = lexer.read()) != Token.EOF; ){
            System.out.println("=> " + token.getText());
        }
    }

}