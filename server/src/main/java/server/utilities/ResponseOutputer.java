package server.utilities;

public class ResponseOutputer {
    private static StringBuilder stringBuilder = new StringBuilder();
    public static void append(Object toOut){
        stringBuilder.append(toOut).append("\n");
    }
    public static String getString(){
        return stringBuilder.toString();
    }
    public static String getOutPut(){
        stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
        String toReturn = stringBuilder.toString();
        stringBuilder.delete(0,stringBuilder.length());
        return toReturn;
    }
}
