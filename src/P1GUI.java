import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class P1GUI {
    JFrame frame;
    JPanel panel;
    FlowLayout flow;
    GridLayout grid;
    ButtonGroup group;
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

    public void generatePanel()
    {
        panel = new JPanel();
    }

    public void endPanel()
    {
        frame.add(panel);

    }

    public void generateFlow(int proccess) {
        flow = new FlowLayout();
        if (proccess == 1) //Frame
            frame.setLayout( flow);
        else if(proccess ==2) //Panel
            panel.setLayout(flow);
    }

    public void generateGrid(int proccess, List<String> gridComp) {
        int len = gridComp.size();
        if (len == 2)
        {
            grid = new GridLayout(Integer.parseInt(gridComp.get(0)),Integer.parseInt(gridComp.get(1)));
            if (proccess == 1) //Frame
                frame.setLayout( grid);
            else if(proccess ==2) //Panel
                panel.setLayout(grid);
        }
        else if  (len == 4)
        {
            grid = new GridLayout(Integer.parseInt(gridComp.get(0)),Integer.parseInt(gridComp.get(1)),Integer.parseInt(gridComp.get(2)),Integer.parseInt(gridComp.get(3)));
            if (proccess == 1) //Frame
                frame.setLayout( grid);
            else if(proccess ==2) //Panel
                panel.setLayout(grid);
        }

    }

    public void generateButton(int proccess, String text){
        JButton button = new JButton(text);
        if(proccess == 1)
            frame.add(button);
        else if (proccess == 2)
            panel.add(button);

    }

    public void generateLabel(int proccess, String text)
    {
        JLabel label = new JLabel(text);
        if(proccess == 1)
            frame.add(label);
        else if (proccess == 2)
            panel.add(label);

    }

    public void generateTextField(int proccess, int num){
        JTextField textField = new JTextField(num);
        if(proccess == 1)
            frame.add(textField);
        else if (proccess == 2)
            panel.add(textField);

    }

    public void generateRadio(int proccess, String text){
        JRadioButton rButton= new JRadioButton(text);
        group.add(rButton);
        if(proccess == 1)
            frame.add(rButton);
        else if (proccess == 2)
            panel.add(rButton);
    }

    public void generateGroup(){
         group = new ButtonGroup();
    }


}
