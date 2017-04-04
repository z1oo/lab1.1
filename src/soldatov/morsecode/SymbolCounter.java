package soldatov.morsecode;


public class SymbolCounter implements Comparable
{
    private long count;
    private char symbol;
    
    public SymbolCounter(char symbol)
    {
        this.symbol = symbol;
        count = 1;
    }

    public int compareTo(Object t)
    {
        return Character.compare(symbol, ((SymbolCounter)t).symbol);
    }
    
    public long count()
    {
        return count;
    }
    
    public char symbol()
    {
        return symbol; 
    }
    
    public void inc()
    {
        count++;
    }
    
    public void inc(long value)
    {
        count += value;
    }
}