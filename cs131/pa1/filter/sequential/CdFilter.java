package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Filter;
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
        setAbsDir(line);
        return null;
    }

    private void setAbsDir(String dirline) {
        String newDir=null;
        switch (dirline){
            case ".":
                return;
            case "..":
                newDir = (new File(SequentialREPL.currentWorkingDirectory)).getParent();
                break;
            default:
                newDir = SequentialREPL.currentWorkingDirectory+ Filter.FILE_SEPARATOR +dirline;
                break;

        }


        File newAbsDir = (new File(newDir));
        if(newAbsDir.exists()){
            SequentialREPL.currentWorkingDirectory=newDir;
        }else{
            System.out.print(Message.DIRECTORY_NOT_FOUND.with_parameter(commandName+" "+dirline));
        }
    }

}
