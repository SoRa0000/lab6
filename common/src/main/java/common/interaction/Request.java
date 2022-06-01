package common.interaction;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private String commandName[];
    private Serializable commandObjectArgument;

    public Request(String[] commandName,Serializable commandObjectArgument){
        this.commandName = commandName;
        this.commandObjectArgument = commandObjectArgument;
    }
    public Request(String[] commandName){
        this(commandName,null);
    }
    public Request(){
        this(new String[]{""},null);
    }

    public String[] getCommandName(){
        return commandName;
    }
    public Object getCommandObjectArgument(){
        return commandObjectArgument;
    }
//    public boolean isEmpty(){
//        return commandName.isEmpty() && commandObjectArgument == null;
//    }
    @Override
    public String toString(){
        return "Request[" + commandName + ", " + commandObjectArgument + "]";
    }

    public boolean isEmpty() {
        return commandName[0].isEmpty() && commandObjectArgument == null;
   }
}
