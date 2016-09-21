package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.util.Queue;

/**
 * Created by rozoa on 9/18/2016.
 */
public class GrepFilter extends SequentialFilterAdvanced {

    private String keyword=null;

    public GrepFilter(Queue<String> inp){
        setInput(inp);
        initializeInOut();
        commandName="grep";
    }

    public GrepFilter(){
        this.input=null;
        initializeInOut();
        commandName="grep";
    }

    @Override
    public void addInput(String parameters) {
        if(keyword==null){
            keyword=parameters;
            //System.out.println("The keyword is "+keyword);
        }else {
            meaninglessLongCommand();
            SequentialCommandBuilder.doesErrorHappen=true;
        }
    }


    @Override
    public void clear() {
        this.keyword=null;
        super.clear();
    }

    @Override
    protected String processLine(String line) {
        return line.contains(keyword) ? line : null;
    }
}
