package soldatov.langtranslator;
        
import java.io.IOException;


public class IllegalTokenException extends IOException
{ 
    public IllegalTokenException()
    {
        super();
    }
    
    public IllegalTokenException(String message)
    {
        super(message);
    }
}
