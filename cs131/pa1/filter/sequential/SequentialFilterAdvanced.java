package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Filter;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by rozoa on 9/18/2016.
 */
public abstract class SequentialFilterAdvanced extends SequentialFilter {

    protected String commandName;


    protected void initializeInOut(){
        input=new LinkedList<>();
        output=new LinkedList<>();

    }

    public void setInput(Queue<String> inp){
        this.input=inp;
    }

    public void addInput(String parameters){
        this.input.add(parameters);
    }

    public String getCommandName(){
        return commandName;
    }

    public void clear(){
        this.input.clear();
        this.output.clear();
        this.prev=null;
        this.next=null;
    }

    public boolean hasNext(){

        return (next!=null);
    }


    public SequentialFilterAdvanced getNext(){
        SequentialFilterAdvanced sfa= (SequentialFilterAdvanced) next;
        return sfa;
    }

    public Queue<String> getOutput(){
        return output;
    }


    protected String processLine(String line) {
        return null;
    }
}
