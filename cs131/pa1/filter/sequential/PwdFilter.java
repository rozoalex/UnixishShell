package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.util.Queue;

public class PwdFilter extends SequentialFilterAdvanced{

    public PwdFilter(Queue<String> inp){
        this.input=inp;
        commandName= "pwd";
    }

    public PwdFilter(){
        this.input=null;
        commandName= "pwd";
    }



    @Override
    protected  String processLine(String line){
        if(input.isEmpty()) {
            return SequentialREPL.currentWorkingDirectory;
        }else{
            System.out.println(Message.CANNOT_HAVE_INPUT.with_parameter(commandName));
            return null;
        }
    }


}
