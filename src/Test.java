import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Test{

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        FileReader inputFile;
        boolean fileFound = false;

        //User enter in a valid file pathway
        while(fileFound == false){
            try{
                System.out.print("Enter a file: ");
                inputFile = new FileReader(input.next());
                Gui gui = new Gui(inputFile);
                fileFound = true;
            }catch(FileNotFoundException e){
                System.out.println("\nYour file not found! Please try again!\n");
            }//end try/catch
        }//end  while loop
    }//end of method

    static class Gui {
        JFrame frame;
        JPanel panel;

        FlowLayout flow;
        GridLayout grid;

        String layoutToken;
        String innerToken;
        String currentToken;

        boolean isToken;
        StringTokenizer tokenizer;

        ArrayList<String> badSymbols = new ArrayList<String>( Arrays.asList("[","(",")",":",
                ".","]",";",","));
        ArrayList<String> tokens = new ArrayList<String>( Arrays.asList("Button", "Group",
                "Label", "Panel", "Textfield",
                "Window", "Layout", "Flow",
                "Grid", "Radio", "End"));

        //Method below reads file and prepares the tokens
        public StringTokenizer readFile(FileReader file){
            Scanner read = new Scanner(file);
            String inputTokens = "";
            String temporary;

            while(read.hasNext()){
                temporary = read.next();
                for(int i = 0; i < badSymbols.size(); i++){
                    temporary = temporary.replace(badSymbols.get(i), "");
                }//for loop
                inputTokens += temporary + " ";
            }//end of while
            return new StringTokenizer(inputTokens);
        }//end of method

        //method to test if the token meets the requirements
        public boolean testToken(int loopLength, String inputToken){
            isToken = false;
            for(int i = 0; i < loopLength; i++){
                if(inputToken.equals(tokens.get(i))){
                    isToken = true;
                }//end of if statement
            }//end of for loop
            return isToken;
        }//end of method

        //method if inner panel occurs
        public void innerPanel(){
            //creating a ew panel and moving to next token
            panel = new JPanel();
            innerToken = tokenizer.nextToken();
            //calling panel layout
            makeLayout(false);

            //testing for valid token
            isToken = testToken(5, innerToken);

            //calling methods in "panel mode"
            while(isToken){

                while(innerToken.equals("Label")){
                    makeLabel(false);
                    innerToken = tokenizer.nextToken();
                }
                while(innerToken.equals("Textfield")){
                    makeTextfield(false);
                    innerToken = tokenizer.nextToken();
                }
                while(innerToken.equals("Button")){
                    makeButton(false);
                    innerToken = tokenizer.nextToken();
                }
                if(innerToken.equals("Group")){
                    makeGroup(false);
                }

                if(innerToken.equals("Panel")){
                    currentToken = innerToken;
                    frame.add(panel);
                    break;
                }
                if(innerToken.equals("End")){
                    makeEnd(false);
                }

                //testing if token is still valid
                isToken = testToken(5, innerToken);

            }//end of while loop
        }//end of method

        //method if end occurs
        public void makeEnd(boolean isFrame){
            if(isFrame){
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }else{
                frame.add(panel);
            }
        }//end of method

        //method to make textfield
        public void makeTextfield(boolean isFrame){
            JTextField textfield = new JTextField(Integer.parseInt(tokenizer.nextToken()));
            if(isFrame){
                frame.add(textfield);
            }else{
                panel.add(textfield);
            }
        }//end of method

        //method to make a button
        public void makeButton(boolean isFrame){
            JButton button = new JButton(tokenizer.nextToken().replace("\"", ""));
            if(isFrame){
                frame.add(button);
            }else{
                panel.add(button);
            }
        }//end of method

        //method to make a label
        public void makeLabel(boolean isFrame){
            JLabel label = new JLabel(tokenizer.nextToken().replace("\"", ""));

            if(isFrame){
                frame.add(label);
            }else{
                panel.add(label);
            }
        }//end of method

        //method to make a JFrame
        public void makeWindow(){
            //creating the window and adding its name
            frame = new JFrame(tokenizer.nextToken().replace("\"", ""));
            frame.setSize(Integer.parseInt(tokenizer.nextToken()), Integer.parseInt(tokenizer.nextToken()));

            currentToken = tokenizer.nextToken();
        }//end of method

        //method to format the layout
        public void makeLayout(boolean isFrame){
            //grabbing the next token for the layout format
            layoutToken = tokenizer.nextToken();

            //testing if layout is grid or flow
            if(layoutToken.equals("Grid")){
                grid = new GridLayout(Integer.parseInt(tokenizer.nextToken()), Integer.parseInt(tokenizer.nextToken()));

                innerToken = tokenizer.nextToken();
                //getting dimensions for the grid
                if(innerToken.matches("[0-9]+")){
                    grid.setVgap(Integer.parseInt(tokenizer.nextToken()));
                    grid.setHgap(Integer.parseInt(innerToken));

                    innerToken = tokenizer.nextToken();
                }//end of if
                //testing if adding grid to a frame or panel
                if(isFrame){
                    frame.setLayout(grid);
                }else{
                    panel.setLayout(grid);
                }//end of if/else
            }else if(layoutToken.equals("Flow")){
                //testing if adding grid to a frame or panel
                if(isFrame){
                    frame.setLayout(new FlowLayout());
                }else{
                    panel.setLayout(new FlowLayout());
                    innerToken = tokenizer.nextToken();
                }//end of if/else
            } //end of flow and grid
        }//end of method

        //method to make a radio button group
        public void makeGroup(boolean isFrame){
            ButtonGroup btnGroup = new ButtonGroup();
            innerToken = tokenizer.nextToken();
            //continuing as long as the token is "radio"
            while(innerToken.equals("Radio")){
                //testing for frame or panel
                if(isFrame){
                    JRadioButton radioBtn = new JRadioButton(tokenizer.nextToken().replace("\"", ""));
                    btnGroup.add(radioBtn);
                    frame.add(radioBtn);
                }else{
                    JRadioButton radioBtn = new JRadioButton(tokenizer.nextToken());
                    btnGroup.add(radioBtn);
                    panel.add(radioBtn);
                }//end of if/else
                innerToken = tokenizer.nextToken();
            }//end of while loop
        }//end of method

        //constructor to piece together the gui
        public Gui(FileReader file){
            //creating list of tokens
            tokenizer = readFile(file);
            innerToken = "";

            //testinf panel value
            while(tokenizer.hasMoreTokens()){
                if(!innerToken.equals("Panel")){
                    currentToken = tokenizer.nextToken();
                }else{
                    currentToken = "Panel";
                }

                //using a switch to decide the next step
                //for whatever reason, the other token types
                //did not work when placed into the switch
                //any feedback on this would be great!
                switch(currentToken){
                    case "Window":
                        makeWindow();
                    case "Layout":
                        makeLayout(true);
                    case "End":
                        makeEnd(true);
                }//switch end

                //testing for other tokens by if statements
                if(currentToken.equals("Label")){
                    makeLabel(true);
                }
                if(currentToken.equals("Button")){
                    makeButton(true);
                }
                if(currentToken.equals("Textfield")){
                    makeTextfield(true);
                }
                if(currentToken.equals("Group")){
                    makeGroup(true);
                }
                if(currentToken.equals("Panel")){
                    innerPanel();
                }

                //looping through to ensure currentToken is valid
                isToken = testToken(11, currentToken);
                if(isToken == false){
                    JOptionPane.showMessageDialog(frame, "Invalid syntax in file caused a parsing error!");
                    System.exit(0);
                }//end of if
            }//end of while loop
        }//end of constructor
    }//end of gui class
}//end of class
