package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.util.*;

//Created by Yuanze Hu

//The main
public class SequentialREPL {

	static String currentWorkingDirectory;
    //stores the current working dir. Can be set by cd.
    static boolean isRunning;
    //if it's true the REPL loop will keep running.
    public static HashMap<String,SequentialFilterAdvanced> commandCollection;
    //command names as keys and different filters are values
    public static boolean doesErrorHappen;
    //indicates there are errors happening inside a filter
    public static SequentialFilterAdvanced head;
    //The first filter
    private static boolean isDone;
    //indicates if the execution process done. Works only for the execution method

	public static void main(String[] args){
	    isRunning = true;
        currentWorkingDirectory=System.getProperty("user.dir");// get working dir
        commandCollection = new HashMap<String,SequentialFilterAdvanced>();
        initializeFilterCollection();//initialize the filters
        Scanner r = new Scanner(System.in);
        System.out.print(Message.WELCOME);
        while(isRunning){
            clearFilters();//wash the filters after using
            //reset the booleans
            isDone = false;
            doesErrorHappen = false;
            head=null;
            //reset the booleans
            System.out.print(Message.NEWCOMMAND);
            String nextCommand=r.nextLine();
            if(!nextCommand.equals("")) {
                doCommand(nextCommand);
            }//process command if the command is not just empty
		}
        r.close();
        System.out.print(Message.GOODBYE);// bye~

	}

	//initialize all supported filters
    //store the names and the corresponding filter in a hashmap
    //call ONLY ONCE at the begining of the main method
    private static void initializeFilterCollection() {
        for(CommandFilters c: CommandFilters.values()){
            commandCollection.put(c.getFilterName(),c.getFilter());
        }
    }

    //the method reset all the filters in the filter collection
    //wash after use
    //call EVERY TIME in the REPL while loop
    private static void clearFilters() {
        for(SequentialFilterAdvanced sfa:commandCollection.values()){
            sfa.clear();
        }
    }



    //method for processing the command
    private static void doCommand(String nextCommand) {
        if(nextCommand.equals("exit")){
            isRunning=false;
            return;
        }//exit has no need to create a filter. It is just hardcoded here.

        head = (SequentialFilterAdvanced) linkFilters(createFiltersFromCommand(nextCommand));
        //executionNonRecursive(head);
        execution(head);
    }


    //seperate commands based on | and >
    private static LinkedList<String> seperateCommand(String rowIn){
        LinkedList<String> commands = new LinkedList<>();
        Scanner commandS = new Scanner(rowIn);

        String temp="";
        int counter=0;
        while (commandS.hasNext()) {
            String cur = commandS.next();
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

                if (!commandS.hasNext()) {
                    commands.add(temp);
                }

            }
            counter++;
        }
        commandS.close();
        return commands;
    }

    //Put command filters in a stack with their inputs
    private static Stack<SequentialFilterAdvanced> createFiltersFromCommand(String rowInp) {
        LinkedList<String> commands = seperateCommand(rowInp);
        Stack<SequentialFilterAdvanced> filterStack = new Stack<>();
        int counter = 0;
        boolean doesOutput =false;
        String prevC=null;
        for(String c:commands) {
            int positionCounter = 0;
            SequentialFilterAdvanced currentFilter = null;
            String [] commandSteps = c.split(" ");
            if(doesOutput){
                //print out the error for cannot have output situation (cd and >)
                System.out.print(Message.CANNOT_HAVE_OUTPUT.with_parameter(prevC));
                currentFilter = null;
                filterStack.clear();
            }

            for (String temp : commandSteps) {
                //String temp = commandScanner.next();
                if (positionCounter == 0 && SequentialREPL.commandCollection.keySet().contains(temp)) {
                    currentFilter = SequentialREPL.commandCollection.get(temp);

                    if(currentFilter instanceof HeadFilter || currentFilter instanceof CdFilter ){
                        if(!filterStack.isEmpty()){
                            System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(c));
                            currentFilter = null;
                            filterStack.clear();
                        }
                    }
                    //detect filters that cannot have output
                    if(currentFilter instanceof CdFilter ||currentFilter instanceof WriteFilter ){
                        doesOutput=true;
                        prevC=c;
                    }
                    //detect filters that cannot have output
                } else if(positionCounter == 0 && !SequentialREPL.commandCollection.keySet().contains(temp)){
                    // if the command is not found in the filter collection hashmap
                    //print out command not found
                    System.out.print(Message.COMMAND_NOT_FOUND.with_parameter(c));
                    currentFilter = null;
                    filterStack.clear();//Clear the Stack if current filter fails
                }
                if (positionCounter != 0 && currentFilter != null) {
                    currentFilter.addInput(temp);
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
        return filterStack;
    }

    //The linkFilters links the filters to a pipeline and setup head
    private static SequentialFilter linkFilters(Stack<SequentialFilterAdvanced> filters){
        SequentialFilter h=null;
        if(filters!=null) {
            if (!filters.isEmpty()) {
                h = filters.pop();
            }

            while (!filters.isEmpty()) {
                SequentialFilterAdvanced current = filters.pop();
                current.setNextFilter(h);
                h = current;
            }
        }
        return h;
    }

    //The execution method recursively process the filters
    //it is the one that does the work
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
