package manager.exceptions;

import java.io.IOException;

public class IOWrapperException extends Exception{
    private final IOException ex;
    private final String fileName;
    public IOWrapperException(String fileName, IOException ex){
        this.fileName = fileName;
        this.ex = ex;
    }

    public String getFileName(){
        return fileName;
    }

    @Override
    public void printStackTrace() {
        ex.printStackTrace();
    }
}
