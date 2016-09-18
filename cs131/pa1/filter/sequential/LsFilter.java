package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.io.File;
import java.util.Queue;

/**
 * fileter for ls command
 *
 */
public class LsFilter extends SequentialFilterAdvanced {


    public LsFilter(Queue<String> inp){
        setInput(inp);
        commandName = "ls";

    }

    public LsFilter(){
        this.input=null;
        commandName = "ls";
    }


    @Override
    protected  String processLine(String line){
        if(input.isEmpty()) {
            File[] listFileNames = (new File(SequentialREPL.currentWorkingDirectory)).listFiles();
            String tempFileNames="";
            int i=listFileNames.length;
            for(File f:listFileNames){
                i--;
                tempFileNames=tempFileNames+f.getName();
                if(i!=0){
                    tempFileNames=tempFileNames+"\n";
                }
            }
            return tempFileNames;

        }else{
            System.out.println(Message.CANNOT_HAVE_INPUT.with_parameter(commandName));
            return null;
        }

    }

    public String getCommandName(){
        return commandName;
    }
}
