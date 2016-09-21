package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.io.File;
import java.util.Queue;

public class CdFilter extends SequentialFilterAdvanced {


    public CdFilter(Queue<String> inp){
        this.input=inp;
        commandName = "cd";
        initializeInOut();
    }

    public CdFilter(){
        this.input=null;
        commandName = "cd";
        initializeInOut();
    }

    @Override
    public void process() {
        if(input.size()==0){
            System.out.print(Message.REQUIRES_PARAMETER.with_parameter(commandName));
        }else if(input.size()==1){
            output.add(processLine(input.poll()));
        }else{
            String temp="cd";
            for(String s:input){
                temp = temp + " "+s;
            }
            System.out.print(Message.COMMAND_NOT_FOUND.with_parameter(temp));
        }
    }

    @Override
    protected String processLine(String line) {
        switch (line){
            case ".":
                return null;
            case "..":
                String parentDir = (new File(SequentialREPL.currentWorkingDirectory)).getParent();
                setDir(parentDir);
                return null;
            default:
                setDir(line);
                return null;
        }

    }

    private void setDir(String dirline) {
        File newDir = (new File(dirline));
        if(newDir.exists()){
            SequentialREPL.currentWorkingDirectory=dirline;
        }else{
            System.out.print(Message.DIRECTORY_NOT_FOUND.with_parameter(this.commandName+" "+dirline));
        }
    }

}
