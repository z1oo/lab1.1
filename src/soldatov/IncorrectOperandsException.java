package soldatov;


public class IncorrectOperandsException extends RuntimeException
{
    public IncorrectOperandsException()
    {
        super();
    }

    public IncorrectOperandsException(String message)
    {
        super(message);
    }
}
