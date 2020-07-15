import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GUIAnalyzer {
    private Stack<String> tokenStack;
    private String token;
    private String contents;
    private P1GUI p1GUI;
    private boolean fromFrame;
    private boolean fromPanel;

    //Regex patterns that are used for the lexer
    private final String regexNum = "\\d+$";
    private final String regexWhiteSpace = "\\s";
    private final String regexLeftParenthesis = "\\(";
    private final String regexRightParenthesis = "\\)";
    private final String regexPeriod = "\\.";
    private final String regexColon = "\\:";
    private final String regexSemiColon = "\\;";
    private final String regexComma = "\\,";
    private final String regexLetter = "[a-z]|[A-Z]";
    private final String regexQuote = "\"";

    public GUIAnalyzer(String fileName) {
        fromFrame = false;
        fromPanel =false;
        token = "";
        contents = new HandleAFile().returnFileContents(fileName);
        tokenStack = new Stack<>();
        p1GUI = new P1GUI();

    }

    /******************************************************
     *Analyzes the incoming text and makes sure it has valid
     *tokens for the syntax we are looking for. Once th
     *******************************************************/
    public boolean lexer() {
        Stack<String> stack = new Stack<>();
        String tempString = ""; //This will be our token builder to push to the stack
        for (int x = 0; x < contents.length(); x++) {
            char aChar = contents.charAt(x);
            String str = String.valueOf(aChar); //Convert char back to string\
            //Check Special symbols
            if (str.matches(regexLeftParenthesis) || str.matches(regexRightParenthesis) || str.matches(regexPeriod) || str.matches(regexSemiColon) || str.matches(regexColon) || str.matches(regexComma)) {
                if (!tempString.isEmpty())
                    stack.push(tempString);
                tempString = "";
                stack.push(str);
            } else if (str.matches(regexWhiteSpace)) {
                //If our custom string is not empty, push our built string to the stack and empty our string
                if (!tempString.isEmpty()) {
                    stack.push(tempString);
                    tempString = "";
                }
            } else if (str.matches(regexQuote)) {

                if (String.valueOf(contents.charAt(x+1)).matches(regexQuote)) // if the next char is a closing quote, add a white space
                {
                    stack.push("");
                }
                continue;
            } else if (str.matches(regexNum) || str.matches(regexLetter)) {
                tempString = tempString + str;
            } else {
                return false; //Invalid Token Detected
            }
        }
        //Finally add the temp stack to our global token stack
        int stackStartSize = stack.size();
        for (int x = 0; x < stackStartSize; x++) {
            tokenStack.push(stack.pop());
        }
        return true;
    }

    public boolean parseTree() {
        if (parseGUI())
            return true; //Now I need to create the gui using the tokens. This is where I am stuck
        else
            return false;
    }

    private boolean parseGUI() {
        List<String> guiComponents = new ArrayList<>();
        token = getNextToken();
        if (token.equals("Window")) {
            token = getNextToken();
            guiComponents.add(token);
            //Here we can assume the next token is a string given that we can pass the value to the token..... I could be wrong.
            token = getNextToken();
            if (token.matches(regexLeftParenthesis)){
                token = getNextToken();
                if (token.matches(regexNum)) // If the pattern matches that of an integer
                {
                    guiComponents.add(token);
                    token = getNextToken();
                    if (token.matches(regexComma)){
                        token = getNextToken();
                        if (token.matches(regexNum)) {
                            guiComponents.add(token);
                            token = getNextToken();
                            if (token.matches(regexRightParenthesis)){
                                token = getNextToken();
                                //from here we can generate thw window
                                p1GUI.generateWindow(guiComponents);
                                fromFrame = true;
                                if (parseLayout()) {
                                    //p1GUI.generateFlow();
                                    token = getNextToken();
                                    if (parseWidgets()) {
                                        //token = getNextToken();
                                        if (token.equals("End")) {
                                            token = getNextToken();
                                            if (token.matches(regexPeriod)){
                                                p1GUI.endWindow();
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
        }
        return false;
    }

    private boolean parseLayout() {
        List<String> layoutComp;
        if (token.equals("Layout")) {
            token = getNextToken();
            if (parseLayout_type()) {
                token = getNextToken();
                if (token.matches(regexColon)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean parseLayout_type() {
        if (token.equals("Flow")) {
                if(fromFrame)
                {
                    p1GUI.generateFlow(1);

                }
                else if (fromPanel)
                {
                    p1GUI.generateFlow(2);
                }
                return true;


        } else if (token.equals("Grid")) {
            List<String> gridComp = new ArrayList<>();
            token = getNextToken();
            if (token.matches(regexLeftParenthesis)){
                token = getNextToken();
                if (token.matches(regexNum)) // If the pattern matches that of an integer
                {
                    gridComp.add(token);
                    token = getNextToken();
                    if (token.matches(regexComma)){
                        token = getNextToken();
                        if (token.matches(regexNum)) {
                            gridComp.add(token);
                            token = getNextToken();
                            if (token.matches(regexComma)){
                                token = getNextToken();
                                if (token.matches(regexNum)) {
                                    gridComp.add(token);
                                    token = getNextToken();
                                    if (token.matches(regexComma)){
                                        token = getNextToken();
                                        if (token.matches(regexNum)) {
                                            gridComp.add(token);
                                            token = getNextToken();
                                        }
                                    }
                                }
                            }
                            if (token.matches(regexRightParenthesis)){
                                if(fromFrame)
                                {
                                    p1GUI.generateGrid(1, gridComp);

                                }
                                else if (fromPanel)
                                {
                                    p1GUI.generateGrid(2, gridComp);
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean parseWidgets() {
        //token = getNextToken();
        if (parseWidget()) {
            token = getNextToken();
            if (parseWidgets()) {
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean parseWidget() {
        if (token.equals("Button")) {
            token = getNextToken();
            String text = token;
            token = getNextToken();
            if (token.matches(regexSemiColon)){
                if(fromFrame)
                    p1GUI.generateButton(1,text);
                else if (fromPanel)
                    p1GUI.generateButton(2,text);
                return true;
            }

        } else if (token.equals("Group")) {
            token = getNextToken();
            p1GUI.generateGroup();
            if (parseRadio_buttons()) {
                token = getNextToken();
                if (token.equals("End")) {
                    token = getNextToken();
                    if (token.matches(regexSemiColon)){

                        return true;
                    }
                }
            }

        } else if (token.equals("Label")) {
            token = getNextToken();
            String text = token;
            token = getNextToken();
            if (token.matches(regexSemiColon)){
                if(fromFrame)
                    p1GUI.generateLabel(1,text);
                else if (fromPanel)
                    p1GUI.generateLabel(2,text);
                return true;
            }

        } else if (token.equals("Panel")) {
            fromFrame = false;
            token = getNextToken();
            fromPanel = true;
            p1GUI.generatePanel(); //From here we can call the panel
            if (parseLayout()) {

                token = getNextToken();
                if (parseWidgets()) {
                    //token = getNextToken();
                    if (token.equals("End")) {
                        token = getNextToken();
                        if (token.matches(regexSemiColon)){
                            p1GUI.endPanel();
                            fromPanel = false;
                            return true;
                        }
                    }
                }
            }
        } else if (token.equals("Textfield")) {
            token = getNextToken();
            if (token.matches(regexNum)) {
                int num = Integer.parseInt(token);
                token = getNextToken();
                if (token.matches(regexSemiColon)){
                    if(fromFrame)
                        p1GUI.generateTextField(1,num);
                    else if (fromPanel)
                        p1GUI.generateTextField(2,num);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean parseRadio_buttons() {
        if (parseRadio_button()) {
            token = getNextToken();
            if (parseRadio_buttons()) {
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean parseRadio_button() {
        if (token.equals("Radio")) {
            token = getNextToken();
            String text = token;
            token = getNextToken();
            if (token.matches(regexSemiColon)){
                if(fromFrame)
                    p1GUI.generateRadio(1,text);
                else if (fromPanel)
                    p1GUI.generateRadio(2,text);
                return true;
            }
        }
        return false;
    }

    private String getNextToken() {

        return tokenStack.pop();
    }

}
