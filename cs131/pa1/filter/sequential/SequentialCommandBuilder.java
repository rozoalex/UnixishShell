package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;

import java.util.*;

public class SequentialCommandBuilder {

	private String rowInput;
	private SequentialFilterAdvanced head;

	public SequentialCommandBuilder(String rowInput){
		this.rowInput = rowInput;
	}


	//interpret the rowinput
	//returns a stack of all filters with parameters
	//the first filter is at the buttom
	private Stack<SequentialFilterAdvanced> createFiltersFromCommand(String rowInp){
		Scanner commandScanner = new Scanner(rowInp);
		Stack<SequentialFilterAdvanced> filterStack=new Stack<>() ;
		int positionCounter=0;
		SequentialFilterAdvanced currentFilter=null;
		while (commandScanner.hasNext()){
			String temp=commandScanner.next();
			if(temp.equals("|")){
				if (currentFilter!=null){
					filterStack.push(currentFilter);
				}
				currentFilter=null;
				positionCounter=0;
			}


			if(positionCounter==0&&SequentialREPL.commandCollection.keySet().contains(temp)){
				currentFilter=SequentialREPL.commandCollection.get(temp);
			}else if(positionCounter!=0&&currentFilter!=null){
				currentFilter.addInput(temp);
			} else{
				System.out.println(Message.COMMAND_NOT_FOUND.with_parameter(temp));
				return null;
			}

			positionCounter++;
		}

		if (currentFilter!=null){
			filterStack.add(currentFilter);
		}


		return filterStack;
	}

	//set up head
	//the first filter to execute
	private void linkFilters(Stack<SequentialFilterAdvanced> filters){
		if(!filters.isEmpty()){
			this.head=filters.pop();
		}

		while(!filters.isEmpty()){
			SequentialFilterAdvanced current = filters.pop();
			current.setNextFilter(head);
			head=current;
		}
	}

	private void execution(SequentialFilterAdvanced sfa){
		if(sfa!=null){
			sfa.process();
		}

		if(sfa.hasNext()){
			execution(sfa.getNext());
		}

	}



	//Client can only call start to execute commands
	public void start() {
		linkFilters(createFiltersFromCommand(this.rowInput));
		execution(this.head);


	}
}
