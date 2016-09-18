package cs131.pa1.filter.sequential;

/**
 * enum of all filters
 * easy to add new commands
 */
public enum CommandFilters {
    LS("ls",new LsFilter()),
    PWD("pwd",new PwdFilter()),
    CD("cd",new CdFilter()),
    HEAD("head",new HeadFilter()),
    GREP("grep",new GrepFilter()),
    WC("wc",new WcFilter()),
    WRITE(">",new WriteFilter()),;

    private SequentialFilterAdvanced sf;
    private final String filterName;

    CommandFilters(String filterName,SequentialFilterAdvanced sf) {
        this.sf=sf;
        this.filterName=sf.getCommandName();

    }

    public SequentialFilterAdvanced getFilter(){
        return sf;
    }

    public String getFilterName(){
        return filterName;
    }
}
