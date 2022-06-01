package server.utilities;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;

public class RequestHandler {
    CommandManager commandManager;
    public RequestHandler(CommandManager commandManager){
        this.commandManager = commandManager;
    }
    public Response handle(Request request){
        ResponseCode responseCode;
        if((request.getCommandName()[0].equals("exit_server"))){
            commandManager.executeCommand(request.getCommandName(),request.getCommandObjectArgument());
            responseCode = ResponseCode.SERVER_EXIT;
        }
        else if(commandManager.executeCommand(request.getCommandName(),request.getCommandObjectArgument())){
            responseCode = ResponseCode.OK;
        } else{
            responseCode = ResponseCode.ERROR;
        }
        return new Response(responseCode,ResponseOutputer.getOutPut());
    }
}
