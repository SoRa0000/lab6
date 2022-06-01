package common.interaction;

import java.io.Serializable;

public class Response implements Serializable{
    private static final long serialVersionUID = 2L;
    private final ResponseCode responseCode;
    private final String responseBody;
   public Response(ResponseCode responseCode, String responseBody){
       this.responseCode = responseCode;
       this.responseBody = responseBody;
   }
   public ResponseCode getResponseCode(){
       return responseCode;
   }
   public String getResponseBody(){
       return responseBody;
   }
    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
