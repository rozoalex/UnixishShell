package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;

import java.util.*;

public class SequentialCommandBuilder {

	private String rowInput;
	private SequentialFilterAdvanced head;
	private boolean isDone=false;
    public static boolean doesErrorHappen=false;

    public SequentialCommandBuilder(String rowInput){
		this.rowInput = rowInput;
	}




	private LinkedList<String> seperateCommand(String rowIn){
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




	//interpret the rowinput
	//returns a stack of all filters with parameters
	//the first filter is at the buttom
	private Stack<SequentialFilterAdvanced> createFiltersFromCommand(String rowInp) {

		//System.out.println("In createFiltersFromCommand,  rowInp is "+rowInp);
		LinkedList<String> commands = seperateCommand(rowInp);
		Stack<SequentialFilterAdvanced> filterStack = new Stack<>();
        int counter = 0;
        for(String c:commands) {
            int positionCounter = 0;
            SequentialFilterAdvanced currentFilter = null;
            Scanner commandScanner = new Scanner(c);
            while (commandScanner.hasNext()) {
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

    private String wholeCommand(String temp, String rowInput ) {
    	Scanner cs = new Scanner(rowInput);
		boolean b =false;
        while(cs.hasNext()) {
			String t = cs.next();
			if (t.equals(temp)) {
				b = true;
			}
			if (b){
				if (t.equals("|") || t.equals(">")) {
					return temp;
				}
			temp = temp + " " + t;
			}
        }
        return temp;
    }

    //set up head
	//the first filter to execute
	private void linkFilters(Stack<SequentialFilterAdvanced> filters){
	    if(filters!=null) {
            if (!filters.isEmpty()) {
                this.head = filters.pop();
            }

            while (!filters.isEmpty()) {
                SequentialFilterAdvanced current = filters.pop();
                current.setNextFilter(head);
                head = current;
            }
        }
	}


	//recursively execute the filters
	private void execution(SequentialFilterAdvanced sfa){
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
	private void printAll(Queue<String> output) {
	    if(!output.isEmpty()) {
            for (String s : output) {
                if(s!=null) {
                    System.out.println(s);
                }
            }
        }
	}


	//Client can only call start to execute commands
	public void start() {
		linkFilters(createFiltersFromCommand(this.rowInput));
		execution(this.head);

	}


}
