This is a like unix shell

It supports some very basic unix command: ls head grep cd wc > pwd

The Filter, Message,and SequentialFilter classes are given.

The main is the SequentialREPL

Each command has one filter, e.g. CdFilter GrepFilter...

All the filters extends the SequentialFilterAdvanced class.

The SequentialFilterAdvanced class extends the SequentialFilter class and adds some useful methods.

The CommandFilters is an enum object. The enum has all commands and corresponding filters.

In the main, a method will iterate through this enum and add and initialize all filters to a hashmap.

The filters once initialized will be using over and over.

The are cleared up in each cycle of REPL.

Not using the SequentialCommandBuilder class.

Everything is done in the SequentialREPL.
 
