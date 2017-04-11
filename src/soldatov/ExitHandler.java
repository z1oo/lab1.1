package soldatov;


public class ExitHandler implements CommandHandler
{
    public void run(String operands[]) throws BreakException
    {
        throw new BreakException();
    }
}
