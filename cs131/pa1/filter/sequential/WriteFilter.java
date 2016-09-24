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

    String outputFile = null;

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
    public void clear() {
        outputFile = null;
        super.clear();
    }

    @Override
    public void addInput(String parameters) {
        if(outputFile==null){
            outputFile=parameters;
        }else{
            System.out.print(Message.INVALID_PARAMETER.with_parameter(commandName));
        }

    }

    @Override
    public void process() {
        if (outputFile==null){
            System.out.print(Message.REQUIRES_PARAMETER.with_parameter(commandName));
            output.clear();
            return;
        }
        processLine(outputFile);
    }

    @Override
    protected String processLine(String line) {
        File f = new File(SequentialREPL.currentWorkingDirectory+ Filter.FILE_SEPARATOR+line);
        if(f.exists()){
            f.delete();
        }


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
            ps.close();
        }catch (FileNotFoundException fnf){
            fnf.printStackTrace();
        }

        return null;
    }
}
