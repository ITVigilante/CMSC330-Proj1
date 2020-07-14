import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class P1GUI {
    JFrame frame;
    JPanel panel;
    FlowLayout flow;
    GridLayout grid;
    public P1GUI(){


    }


    public void generateWindow (List<String> windowComps)
    {
        frame = new JFrame(windowComps.get(0));
        frame.setSize(Integer.parseInt(windowComps.get(1)), Integer.parseInt(windowComps.get(2)));
    }

    public void endWindow()
    {
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
