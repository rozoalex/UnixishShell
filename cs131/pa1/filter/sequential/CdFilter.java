package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.io.File;
import java.util.Queue;

public class CdFilter extends SequentialFilterAdvanced {


    public CdFilter(Queue<String> inp){
        this.input=inp;
        commandName = "cd";
    }

    public CdFilter(){
        this.input=null;
        commandName = "cd";
    }

    @Override
    public void process() {
        if(input.size()==1){
            System.out.println(Message.REQUIRES_PARAMETER.with_parameter(commandName));
        }else if(input.size()==2){
            output.add(processLine(input.poll()));
            output.add(processLine(input.poll()));
        }else{

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
            System.out.println(Message.DIRECTORY_NOT_FOUND.with_parameter(this.commandName+" "+dirline));
        }
    }

}
