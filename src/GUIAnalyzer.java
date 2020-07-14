import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.logging.FileHandler;

public class ParseTree {
    private Stack<String> tokenStack;
    private String token;
    private String contents;

    //Regex patterns that are used for the lexer
    private final String regexNum ="\\d+$";
    private final String regexWhiteSpace = "\\s";
    private final String regexLeftParenthesis="\\(";
    private final String regexRightParenthesis="\\)";
    private final String regexPeriod = "\\.";
    private final String regexColon = "\\:";
    private final String regexSemiColon = "\\;";
    private final String regexComma = "\\,";
    private final String regexLetter = "[a-z]|[A-Z]";
    private final String regexQuote = "\"";
    public ParseTree(String fileName)
    {
        token = "";
        contents = new HandleAFile().returnFileContents(fileName);
        tokenStack = new Stack<>();
        lexer();
        parseGui();
    }

    /******************************************************
    *Analyzes the incoming text and makes sure it has valid
    *tokens for the syntax we are looking for. Once th
    *******************************************************/
    private boolean lexer()
    {
        Stack <String> stack = new Stack<>();
        String tempString = ""; //This will be our token builder to push to the stack
        for (int x=0; x < contents.length(); x++ )
        {
            char aChar = contents.charAt(x);
            String str = String.valueOf(aChar); //Convert char back to string\
            //Check Special symbols
            if(str.matches(regexLeftParenthesis)||str.matches(regexRightParenthesis)||str.matches(regexPeriod)||str.matches(regexSemiColon)||str.matches(regexColon)||str.matches(regexComma)||str.matches(regexQuote))
            {
                if(!tempString.isEmpty())
                    stack.push(tempString);
                tempString = "";
                stack.push(str);
            }
            else if(str.matches(regexWhiteSpace)) {
                //If our custom string is not empty, push our built string to the stack and empty our string
                if(!tempString.isEmpty())
                {
                    stack.push(tempString);
                    tempString = "";
                }
            }
            else if(str.matches(regexNum) || str.matches(regexLetter)) {
                tempString = tempString + str;
            }
            else{
                return false; //Invalid Token Detected
            }
        }

        //Finally add the temp stack to our global token stack
        int stackStartSize = stack.size();
        for(int x = 0; x<stackStartSize;x++)
        {
            tokenStack.push(stack.pop());
        }
        return true;
    }

    public String getNextToken()
    {

        return tokenStack.pop();
    }

    public boolean parseGui()
    {
        if (token == "Window")
        {
            token = getNextToken();
            //Here we can assume the next token is a string given that we can pass the value to the token..... I could be wrong.
            token = getNextToken();
            if (token == "(")
            {
                token = getNextToken();
                if(token.matches("^\\d+$")) // If the pattern matches that of an integer
                {
                    token = getNextToken();
                    if (token == ",")
                    {
                        token = getNextToken();
                        if(token.matches("^\\d+$"))
                        {
                            token = getNextToken();
                            if (token == ")")
                            {
                                token = getNextToken();
                                if (parseLayout())
                                {
                                    token = getNextToken();
                                    if(parseWidgets())
                                    {
                                        token=getNextToken();
                                        if (token == "End.")
                                        {
                                            return true;
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean parseLayout()
    {
        token = getNextToken();
        if (token == "Layout")
        {
           token = getNextToken();
           if (parseLayout_type())
           {
               if (token == ":")
               {
                   return true;
               }
           }
        }

        return false;
    }

    public boolean parseLayout_type()
    {
        token = getNextToken();
        if( token == "Flow")
        {
            return true;
        }
        else if(token == "Grid")
        {
            token = getNextToken();
            if (token == "(")
            {
                token = getNextToken();
                if(token.matches("^\\d+$")) // If the pattern matches that of an integer
                {
                    token = getNextToken();
                    if (token == ",")
                    {
                        token = getNextToken();
                        if(token.matches("^\\d+$"))
                        {
                            token = getNextToken();
                            if (token == ",")
                            {
                                token = getNextToken();
                                if(token.matches("^\\d+$"))
                                {
                                    token = getNextToken();
                                    if (token == ",")
                                    {
                                        token = getNextToken();
                                        if(token.matches("^\\d+$"))
                                        {
                                            token = getNextToken();
                                        }

                                    }
                                }

                            }
                            if (token == ")")
                            {
                               return true;
                            }
                        }
                    }
                }
            }

        }
        return false;
    }

    public boolean parseWidgets()
    {
        //token = getNextToken();
        if (parseWidget())
        {
            token = getNextToken();
            if (parseWidgets())
            {
                return true;
            }
            return true;
        }
        return false;
    }

    public boolean parseWidget()
    {
        token = getNextToken();
        if (token == "Button")
        {
            token = getNextToken();
            token = getNextToken();
            if(token == ";")
            {
                return true;
            }

        }
        else if (token == "Group")
        {
            token = getNextToken();
            if(parseRadio_buttons())
            {
                token = getNextToken();
                if(token == "End;")
                {
                    return true;
                }
            }

        }
        else if (token == "Label")
        {
            token = getNextToken();
            token = getNextToken();
            if(parseRadio_buttons())
            {
                token = getNextToken();
                if(token == ";")
                {
                    return true;
                }
            }

        }
        else if (token == "Panel")
        {
            token = getNextToken();
            if(parseLayout())
            {
                token = getNextToken();
                if(parseWidgets())
                {
                    token = getNextToken();
                    if(token == "End;")
                    {
                        return true;
                    }
                }
            }
        }
        else if (token == "Textfield")
        {
            token = getNextToken();
            if(token.matches("^\\d+$"))
            {
                token = getNextToken();
                if(token == ";")
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean parseRadio_buttons()
    {
        if (parseRadio_button())
        {
            token = getNextToken();
            if (parseRadio_buttons())
            {
                return true;
            }
            return true;
        }
        return false;
    }

    public boolean parseRadio_button()
    {
        if(token == "Radio")
        {
            token = getNextToken();
            if(token == ";")
            {
                return true;
            }
        }
        return false;
    }

}
