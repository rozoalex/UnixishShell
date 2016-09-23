package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.io.File;
import java.util.Queue;

/**
 * fileter for ls command
 *
 */
public class LsFilter extends SequentialFilterAdvanced {


    //public LsFilter(Queue<String> inp){
    //    setInput(inp);
    //    commandName = "ls";
    //    initializeInOut();
    //}//useless constructor

    public LsFilter(){
        this.input=null;
        commandName = "ls";
        initializeInOut();
    }

    @Override
    public void process(){
        if(input.isEmpty()) {
            File[] listFileNames = (new File(SequentialREPL.currentWorkingDirectory)).listFiles();
            String tempFileNames="";
            for(File f:listFileNames){
                output.add(f.getName());
            }
        }else{
            System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(commandName));
        }
    }



}
