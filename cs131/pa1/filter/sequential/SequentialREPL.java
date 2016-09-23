package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.util.*;

public class SequentialREPL {

	static String currentWorkingDirectory;
    //static private final String welcomeWords="> Welcome to the Unix-ish command line.";
    //static private final String byeWords="Thank you for using the Unix-ish command line. Goodbye!";
	static boolean isRunning;
    public static HashMap<String,SequentialFilterAdvanced> commandCollection;
    public static boolean doesErrorHappen;
    public static SequentialFilterAdvanced head;
    private static boolean isDone;

	public static void main(String[] args){

	    isRunning = true;
        isDone = false;
	    doesErrorHappen = false;
	    commandCollection = new HashMap();
        currentWorkingDirectory=System.getProperty("user.dir");
        //System.out.println(currentWorkingDirectory);
        updateCommands();
        Scanner r = new Scanner(System.in);
        System.out.print(Message.WELCOME);
        while(isRunning){
            System.out.print(Message.NEWCOMMAND);
            String nextCommand=r.nextLine();
            if(!nextCommand.equals("")) {
                doCommand(nextCommand);
            }
            head=null;
            //System.out.println();

		}

		//System.out.print(Message.NEWCOMMAND);

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
    private static void doCommand(String nextCommand) {
        if(nextCommand.equals("exit")){
            isRunning=false;
            return;
        }
        //SequentialCommandBuilder scb = new SequentialCommandBuilder(nextCommand);
        //scb.start();

        linkFilters(createFiltersFromCommand(nextCommand));
        execution(head);
    }



    private static LinkedList<String> seperateCommand(String rowIn){
        LinkedList<String> commands = new LinkedList<>();
        Scanner commandScanner = new Scanner(rowIn);

        String temp="";
        int counter=0;
        while (commandScanner.hasNext()) {
            String cur = commandScanner.next();
            if (cur.equals("|")) {
                commands.add(temp);
                temp = "";
            } else {
                if(cur.equals(">")&&(counter!=0)){
                    commands.add(temp);
                    temp = "";
                }

                if (temp.equals("")) {
                    temp = temp + cur;
                } else {
                    temp = temp + " " + cur;
                }

                if (!commandScanner.hasNext()) {
                    commands.add(temp);
                }

            }
            counter++;
        }

        return commands;
    }

    private static Stack<SequentialFilterAdvanced> createFiltersFromCommand(String rowInp) {

        //System.out.println("In createFiltersFromCommand,  rowInp is "+rowInp);
        LinkedList<String> commands = seperateCommand(rowInp);
        Stack<SequentialFilterAdvanced> filterStack = new Stack<>();
        int counter = 0;
        for(String c:commands) {
            int positionCounter = 0;
            SequentialFilterAdvanced currentFilter = null;
            Scanner commandScanner = new Scanner(c);
            while (commandScanner.hasNext()) {
                //System.out.print("what's up");//testing
                String temp = commandScanner.next();
                if (positionCounter == 0 && SequentialREPL.commandCollection.keySet().contains(temp)) {
                    currentFilter = SequentialREPL.commandCollection.get(temp);
                    //System.out.println("setup filter " + currentFilter.getCommandName());
                } else if(positionCounter == 0 && !SequentialREPL.commandCollection.keySet().contains(temp)){
                    System.out.print(Message.COMMAND_NOT_FOUND.with_parameter(c));
                    currentFilter = null;
                    filterStack.clear();//Clear the Stack if current filter fails
                }
                if (positionCounter != 0 && currentFilter != null) {
                    currentFilter.addInput(temp);
                    //System.out.println("add inp " + temp);
                }



                if (counter == 0 && (temp.equals("grep") || temp.equals("wc") || temp.equals(">"))) {
                    System.out.print(Message.REQUIRES_INPUT.with_parameter(c));
                    currentFilter = null;
                    filterStack.clear();//Clear the Stack if current filter fails
                }


                positionCounter++;


                if (doesErrorHappen) {
                    doesErrorHappen = false;
                    currentFilter = null;
                    filterStack.clear();
                }
            }

            counter++;
            if (currentFilter != null) {
                filterStack.add(currentFilter);
            }
        }


        //System.out.println(filterStack.peek().getCommandName());
        return filterStack;
    }

    private static void linkFilters(Stack<SequentialFilterAdvanced> filters){
        if(filters!=null) {
            if (!filters.isEmpty()) {
                head = filters.pop();
            }

            while (!filters.isEmpty()) {
                SequentialFilterAdvanced current = filters.pop();
                current.setNextFilter(head);
                head = current;
            }
        }
    }

    private static void execution(SequentialFilterAdvanced sfa){
        if(sfa!=null){
            sfa.process();
            if(sfa.getNext()!=null) {
                sfa.getNext().setInput(sfa.output);
            }
            execution(sfa.getNext());
            if(isDone){
                //System.out.println("output peek is "+sfa.getOutput().peek());
                printAll(sfa.getOutput());

                isDone=false;
            }
            sfa.clear();//clear the filter for reuse
        }else{
            isDone=true;
        }


    }

    //method for print the final result
    private static void printAll(Queue<String> output) {
        if(!output.isEmpty()) {
            for (String s : output) {
                if(s!=null) {
                    System.out.println(s);
                }
            }
        }
    }




}
