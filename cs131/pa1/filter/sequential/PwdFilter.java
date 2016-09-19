package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.util.Queue;

public class PwdFilter extends SequentialFilterAdvanced{

    public PwdFilter(Queue<String> inp){
        this.input=inp;
        commandName= "pwd";
        initializeInOut();
    }

    public PwdFilter(){
        this.input=null;
        commandName= "pwd";
        initializeInOut();
    }



    @Override
    public void process(){
        if (input.isEmpty()){
            String processedLine = processLine();
            if (processedLine != null){
                output.add(processedLine);
            }
        }else{
            System.out.println(Message.CANNOT_HAVE_INPUT.with_parameter(commandName));
        }
    }



    private String processLine(){
        return SequentialREPL.currentWorkingDirectory;
    }


}
