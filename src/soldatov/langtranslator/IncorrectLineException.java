package soldatov.langtranslator;

import java.io.IOException;


public class IncorrectLineException extends IOException
{
    public IncorrectLineException()
    {
        super();
    }

    public IncorrectLineException(String message)
    {
        super(message);
    }
}
