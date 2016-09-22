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


	//interpret the rowinput
	//returns a stack of all filters with parameters
	//the first filter is at the buttom
	private Stack<SequentialFilterAdvanced> createFiltersFromCommand(String rowInp){
		//rowInp=rowInp.replaceAll("|"," | ");
		Scanner commandScanner = new Scanner(rowInp);
		//System.out.println("In createFiltersFromCommand,  rowInp is "+rowInp);
		Stack<SequentialFilterAdvanced> filterStack=new Stack<>() ;
		int positionCounter=0;
        int counter=0;
		SequentialFilterAdvanced currentFilter=null;
		while (commandScanner.hasNext()){
			String temp=commandScanner.next();
			//System.out.println("------temp is "+temp);
			if(temp.equals("|")){
				if (currentFilter!=null){
					filterStack.push(currentFilter);
				}
				currentFilter=null;
				positionCounter=0;
			}else {
				if(temp.equals(">")){
					filterStack.push(currentFilter);
					currentFilter=null;
					positionCounter=0;
				}
                //System.out.println(positionCounter == 0 && SequentialREPL.commandCollection.keySet().contains(temp));
				if (positionCounter == 0 && SequentialREPL.commandCollection.keySet().contains(temp)) {
					currentFilter = SequentialREPL.commandCollection.get(temp);
					//System.out.println("setup filter " + currentFilter.getCommandName());
				} else if (positionCounter != 0 && currentFilter != null) {
					currentFilter.addInput(temp);
					//System.out.println("add inp " + temp);
				} else {
					System.out.print(Message.COMMAND_NOT_FOUND.with_parameter(temp));
					return null;
				}

				if(counter==0 &&(temp.equals("grep")|| temp.equals("wc")|| temp.equals(">"))){
                    System.out.print(Message.REQUIRES_INPUT.with_parameter(temp));
                    return null;
                }
                positionCounter++;
			}

            counter++;
            if(doesErrorHappen){
            	doesErrorHappen=false;
                return null;
            }
		}

		if (currentFilter!=null){
			filterStack.add(currentFilter);
		}


		//System.out.println(filterStack.peek().getCommandName());
		return filterStack;
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
