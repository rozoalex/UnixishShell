package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Queue;

/**
 * Created by rozoa on 9/18/2016.
 */
public class WriteFilter extends SequentialFilterAdvanced {

    public WriteFilter(Queue<String> inp){
        setInput(inp);
        initializeInOut();
        commandName=">";
    }

    public WriteFilter(){
        this.input=null;
        initializeInOut();
        commandName=">";
    }

    @Override
    public void process() {
        if(input.size()==0){
            System.out.println(Message.REQUIRES_INPUT.with_parameter(commandName));
        }else if(input.size()==1){
            processLine(input.poll());
        }else {
            System.out.println(Message.INVALID_PARAMETER.with_parameter(commandName));
        }
    }

    @Override
    protected String processLine(String line) {
        File f = new File(SequentialREPL.currentWorkingDirectory+ Filter.FILE_SEPARATOR+line);
        if(f.exists()){
            f.delete();
        }

        f.mkdirs();
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PrintStream ps = new PrintStream(f);
            while(!input.isEmpty()){
                ps.println(input.poll());
            }
        }catch (FileNotFoundException fnf){
            fnf.printStackTrace();
        }




        return null;
    }
}
