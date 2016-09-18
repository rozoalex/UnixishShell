package cs131.pa1.filter.sequential;

import java.util.Queue;

/**
 * Created by rozoa on 9/18/2016.
 */
public class HeadFilter extends SequentialFilterAdvanced {

    public HeadFilter(Queue<String> inp){
        setInput(inp);
    }

    public HeadFilter(){
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
