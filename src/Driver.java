public class Driver {
    public static void main (String[] args)
    {

        GUIAnalyzer guiBuilder = new GUIAnalyzer("Test.txt");
        if(guiBuilder.lexer()) {
            if (guiBuilder.parseTree())
                System.out.println("Success");
            else
                System.out.println("ParseTree Failed");
        }
        else {
            System.out.println("Lexer Failed");
        }
    }
}
