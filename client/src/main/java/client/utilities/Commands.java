package client.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Commands {
    private String commandName;
    private int commandLength;
    private ProcessingCode processingCode;

    public Commands(String commandName, int commandLength, ProcessingCode processingCode) {
        this.commandName = commandName;
        this.commandLength = commandLength;
        this.processingCode = processingCode;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public int getCommandLength() {
        return commandLength;
    }

    public void setCommandLength(int commandLength) {
        this.commandLength = commandLength;
    }

    public ProcessingCode getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(ProcessingCode processingCode) {
        this.processingCode = processingCode;
    }
}
