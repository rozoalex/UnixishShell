package cs131.pa1.filter.sequential;

import java.util.Queue;

/**
 * Created by rozoa on 9/18/2016.
 */
public class WriteFilter extends SequentialFilterAdvanced {

    public WriteFilter(Queue<String> inp){
        setInput(inp);
    }

    public WriteFilter(){
        this.input=null;
    }

    public void setInput(Queue<String> inp){
        this.input=inp;
    }

    @Override
    protected String processLine(String line) {
        return null;
    }
}
