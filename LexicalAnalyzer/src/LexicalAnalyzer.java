import java.io.*;
import java.util.*;

public class LexicalAnalyzer
{
    private static final int LETTER = 0;
    private static final int DIGIT = 1;
    private static final int UNKNOWN = 99;
    private static final int EOF = -1;
    private static final int INT_LIT = 10;
    private static final int IDENT = 11;
    private static final int ASSIGN_OP = 20;
    private static final int ADD_OP = 21;
    private static final int SUB_OP = 22;
    private static final int MULT_OP = 23;
    private static final int DIV_OP = 24;
    private static final int LEFT_PAREN = 25;
    private static final int RIGHT_PAREN = 26;
    private static final int SEMI_COLON = 27;


    private static int charClass;
    private static char lexeme[] = {0};
    private static char nextChar;
    private static int lexLen;
    private static int token;
    private static int nextToken;
    private static File file;
    private static FileInputStream fs;
    String directory = "C:\\Users\\manoj\\Documents\\PLAssignment5\\LexicalAnalyzer\\files";
    String filename = "input.txt";
    String absolutepath = directory + File.separator + filename;

    public static void main(String args[])
    {
        LexicalAnalyzer obj = new LexicalAnalyzer();
        lexLen=0;
        lexeme=new char[100];
        file = new File(obj.absolutepath);
        if (!file.exists())
        {
            System.out.println( "File does not exist.");
            return;
        }
        if (!(file.isFile() && file.canRead()))
        {
            System.out.println("Unable to read from "+file.getName());
            return;
        }
        try
        {
            fs = new FileInputStream(file);
            char current;
            getChar();
            while (fs.available() > 0)
            {
                lex();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static int lookup(char ch)
    {
        switch (ch)
        {
            case '(':
                addChar();
                nextToken = LEFT_PAREN;
                break;
            case ')':
                addChar();
                nextToken = RIGHT_PAREN;
                break;
            case '+':
                addChar();
                nextToken = ADD_OP;
                break;
            case '-':
                addChar();
                nextToken = SUB_OP;
                break;
            case '*':
                addChar();
                nextToken = MULT_OP;
                break;
            case '/':
                addChar();
                nextToken = DIV_OP;
                break;
            case '=':
                addChar();;
                nextToken = ASSIGN_OP;
                break;
            case ';':
                addChar();
                nextToken = SEMI_COLON;
                break;
            default:
                addChar();
                nextToken = EOF;
                break;
        }
        return nextToken;
    }
    public static void addChar()
    {
        if (lexLen <= 98)
        {
            lexeme[lexLen++] = nextChar;
            // lexeme[lexLen] = 0;
        }
        else
            System.out.println("Error : Lexeme is too long to parse\n");
    }
    public static void getChar()
    {
        try
        {
            if(fs.available()>0)
            {
                nextChar=(char)fs.read();
                if(Character.isLetter(nextChar))
                    charClass=LETTER;
                else if(Character.isDigit(nextChar))
                    charClass=DIGIT;
                else
                    charClass=UNKNOWN;
            }
            else
                charClass=EOF;

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void getNonBlank()
    {
        while(Character.isSpaceChar(nextChar))
            getChar();

    }
    public static int lex()
    {
        lexLen = 0;
        getNonBlank();
        switch (charClass)
        {
            case LETTER:
                addChar();
                getChar();
                while (charClass == LETTER || charClass == DIGIT)
                {
                    addChar();
                    getChar();
                }
                nextToken = IDENT;
                break;
            case DIGIT:
                addChar();
                getChar();
                while(charClass == DIGIT)
                {
                    addChar();
                    getChar();
                }
                nextToken = INT_LIT;
                break;
            case UNKNOWN:
                lookup(nextChar);
                getChar();
                break;
            case EOF:
                nextToken = EOF;
                lexeme[0] = 'E';
                lexeme[1] = 'O';
                lexeme[2] = 'F';
                lexeme[3] = 0;
                break;

        } /* end of switch */
        System.out.print("Next token is :"+nextToken+" Next lexeme is :");
        for(int i=0;i<lexLen;i++)
            System.out.print(lexeme[i]);
        System.out.println();
        return nextToken;
    }
}

