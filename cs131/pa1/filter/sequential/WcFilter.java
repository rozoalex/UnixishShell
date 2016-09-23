package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.util.Queue;
import java.util.Scanner;

/**
 * Created by rozoa on 9/18/2016.
 */
public class WcFilter extends SequentialFilterAdvanced {

    public WcFilter(Queue<String> inp){
        setInput(inp);
        initializeInOut();
        commandName="wc";
    }

    public WcFilter(){
        this.input=null;
        initializeInOut();
        commandName="wc";
    }

    @Override
    public void addInput(String parameters) {
        SequentialREPL.doesErrorHappen=true;
        System.out.println(Message.INVALID_PARAMETER.with_parameter(commandName));
    }

    @Override
    public void process() {
            if(input==null){
                return;
            }

            if(prev==null){
                output.clear();
                return;
            }

            String out = "";
            out = out + String.valueOf(input.size());
            int numbOfWords = 0;
            int numbOfChars = 0;
            while (!input.isEmpty()) {
                String temp = input.poll();
                numbOfChars = numbOfChars + temp.length();
                Scanner tempScan = new Scanner(temp);
                while (tempScan.hasNext()) {
                    numbOfWords++;
                    String word = tempScan.next();
                }
            }
            out = out + " " + String.valueOf(numbOfWords) + " " + String.valueOf(numbOfChars);
            output.add(out);

    }


}
