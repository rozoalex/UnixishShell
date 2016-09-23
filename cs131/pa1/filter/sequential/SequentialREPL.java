package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

import java.util.*;

public class SequentialREPL {

	static String currentWorkingDirectory;
    static boolean isRunning;
    public static HashMap<String,SequentialFilterAdvanced> commandCollection;
    public static boolean doesErrorHappen;
    public static SequentialFilterAdvanced head;
    private static boolean isDone;
    //public static String currentCommand=null;

	public static void main(String[] args){
	    isRunning = true;
        isDone = false;
	    doesErrorHappen = false;
	    commandCollection = new HashMap();
        head=null;
        currentWorkingDirectory=System.getProperty("user.dir");
//        System.out.println(currentWorkingDirectory);
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

        r.close();
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
        commandScanner.close();
        return commands;
    }

    private static Stack<SequentialFilterAdvanced> createFiltersFromCommand(String rowInp) {

        //boolean haveCD=false ;
        LinkedList<String> commands = seperateCommand(rowInp);
        Stack<SequentialFilterAdvanced> filterStack = new Stack<>();
        int counter = 0;
        boolean doesCdOutput =false;
        String prevC=null;
        for(String c:commands) {

            //currentCommand=c;
            int positionCounter = 0;
            SequentialFilterAdvanced currentFilter = null;
            Scanner commandScanner = new Scanner(c);
            if(doesCdOutput){

                System.out.print(Message.CANNOT_HAVE_OUTPUT.with_parameter(prevC));
                currentFilter = null;
                filterStack.clear();
            }

            while (commandScanner.hasNext()) {
                String temp = commandScanner.next();
                if (positionCounter == 0 && SequentialREPL.commandCollection.keySet().contains(temp)) {
                    currentFilter = SequentialREPL.commandCollection.get(temp);

                    if(currentFilter instanceof HeadFilter || currentFilter instanceof CdFilter ){
                        if(!filterStack.isEmpty()){
                            System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(c));
                            currentFilter = null;
                            filterStack.clear();
                        }
                    }

                    if(currentFilter instanceof CdFilter ){
                        doesCdOutput=true;
                        prevC=c;
                    }





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
