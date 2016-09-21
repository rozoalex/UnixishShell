package cs131.pa1.filter.sequential;

/**
 * enum of all filters
 * easy to add new commands
 */
public enum CommandFilters {
    LS(new LsFilter()),
    PWD(new PwdFilter()),
    CD(new CdFilter()),
    HEAD(new HeadFilter()),
    GREP(new GrepFilter()),
    WC(new WcFilter()),
    WRITE(new WriteFilter()),;

    private SequentialFilterAdvanced sf;
    private final String filterName;

    CommandFilters(SequentialFilterAdvanced sf) {
        this.sf=sf;
        this.filterName=sf.getCommandName();
        //System.out.println(this.filterName);

    }

    public SequentialFilterAdvanced getFilter(){
        return sf;
    }

    public String getFilterName(){
        return filterName;
    }
}
