package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.util.HashMap;
import java.util.Scanner;

public class SequentialREPL {

	static String currentWorkingDirectory;
    //static private final String welcomeWords="> Welcome to the Unix-ish command line.";
    //static private final String byeWords="Thank you for using the Unix-ish command line. Goodbye!";
	static boolean isRunning = true;
    public static HashMap<String,SequentialFilterAdvanced> commandCollection = new HashMap();

	public static void main(String[] args){

        currentWorkingDirectory=System.getProperty("user.dir");
        System.out.println(currentWorkingDirectory);
        updateCommands();
        Scanner r = new Scanner(System.in);
        System.out.print(Message.WELCOME);
        while(isRunning){
            System.out.print(Message.NEWCOMMAND);
            String nextCommand=r.nextLine();
            if(!nextCommand.equals("")) {
                doCommand(nextCommand);
            }
            //System.out.println();
		}
        System.out.print(Message.GOODBYE);

	}

	//set up all the supported commands
    //store the names and the corresponding filter in a hashmap
    private static void updateCommands() {
        for(CommandFilters c: CommandFilters.values()){
            commandCollection.put(c.getFilterName(),c.getFilter());
        }
    }


    //method for executing the command
    public static void doCommand(String nextCommand) {
        if(nextCommand.equals("exit")){
            isRunning=false;
            return;
        }
        SequentialCommandBuilder scb = new SequentialCommandBuilder(nextCommand);
        scb.start();



    }

}
