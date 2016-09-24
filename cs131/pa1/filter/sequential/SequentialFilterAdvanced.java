package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The abstract class extends the sequential filter abstract class and adds lots of useful methods
 * The class is extended by each specific filter
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

    public void addAllInput(Queue<String> inp){
        this.input.addAll(inp);
    }

    public void addInput(String parameters){
        this.input.add(parameters);
    }

    public void setNextInp(Queue<String> inp){
            if (this.output == null) {
                this.output = new LinkedList<String>();
            }
            if (this.next instanceof SequentialFilterAdvanced) {
                SequentialFilterAdvanced nextSFA = (SequentialFilterAdvanced) this.next;
                nextSFA.addAllInput(inp);
            }

    }

    public String getCommandName(){
        return commandName;
    }

    public void clear(){
        initializeInOut();
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

    public SequentialFilterAdvanced getPrev(){
        SequentialFilterAdvanced sfa= (SequentialFilterAdvanced) prev;
        return sfa;
    }

    public Queue<String> getOutput(){
        return output;
    }

    protected void meaninglessLongCommand(){
        String temp=this.getCommandName();
        for(String s:input){
            temp = temp + " "+s;
        }
        System.out.print(Message.INVALID_PARAMETER.with_parameter(temp));

    }


    protected String processLine(String line)  {
        return null;
    }
}
