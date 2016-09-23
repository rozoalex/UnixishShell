package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.Scanner;


public class HeadFilter extends SequentialFilterAdvanced {



    private int numberOfOutputLines;//default is 10
    private boolean haveNumberPara=false;

    public HeadFilter(Queue<String> inp){
        setInput(inp);
        initializeInOut();
        numberOfOutputLines=10;
        commandName="head";
    }

    public HeadFilter(){
        this.input=null;
        initializeInOut();
        numberOfOutputLines=10;
        commandName="head";
    }


    @Override
    public void clear() {
        haveNumberPara=false;
        numberOfOutputLines=10;
        super.clear();
    }

    @Override
    public void process() {
       // System.out.println("head head head");
        if(input.size()>2){
            meaninglessLongCommand();
        }else if (input.size()==1){
            processLine(input.poll());
        }else if(input.size()==2){
            String temp=input.poll();
            if(isNumeric(temp)){
                haveNumberPara=true;
                numberOfOutputLines=Math.abs(Integer.parseInt(temp));
            }else{
                meaninglessLongCommand();
            }
            processLine(input.poll());

        }else if(input.size()==0){
            System.out.print(Message.REQUIRES_PARAMETER.with_parameter(commandName));
        }

    }

    //check if a string can be a double
    private boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    @Override
    protected String processLine(String fileName) {
        try {
            Scanner readFile = new Scanner(new File(SequentialREPL.currentWorkingDirectory+ Filter.FILE_SEPARATOR+fileName));
            int counter=0;
            while(readFile.hasNextLine()&&(counter!=numberOfOutputLines)){
                output.add(readFile.nextLine());
                //System.out.println(output.peek());
                counter++;
            }

        }catch (FileNotFoundException e){
            try{
                Scanner readFile = new Scanner(new File(SequentialREPL.currentWorkingDirectory+ Filter.FILE_SEPARATOR+fileName.replaceAll("_"," ")));
                int counter=0;
                while(readFile.hasNextLine()&&(counter!=numberOfOutputLines)) {
                    output.add(readFile.nextLine());
                    //System.out.println(output.peek());
                    counter++;
                }
            }catch(FileNotFoundException h){
                    if(next!=null){
                        SequentialFilterAdvanced sfa = (SequentialFilterAdvanced) next;
                        sfa.output=null;
                    }
                    if(!haveNumberPara) {
                        System.out.print(Message.FILE_NOT_FOUND.with_parameter(commandName + " " + fileName));
                    }else{
                        System.out.print(Message.FILE_NOT_FOUND.with_parameter(commandName + " -"+numberOfOutputLines+ " " + fileName));
                    }

                    }


        }

        return null;

    }
}
