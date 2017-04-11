package soldatov.morsecode;

import java.io.*;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import soldatov.langtranslator.*;


public class MorseCoder implements LanguageTranslator
{
    private final TreeMap<Character, String> dictionary;
    private static final Pattern dictPattern = Pattern.compile("(.)\\s+([\\-\\.]+)");


    public MorseCoder(Reader dictStream) throws IllegalTokenException, IOException,
                                                  NullPointerException
    {
        if (dictStream == null)
        {
            throw new NullPointerException("dictStream can't be null");
        }
        
        dictionary = new TreeMap<Character, String>();
        
        BufferedReader reader = new BufferedReader(dictStream);
        
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            Matcher pair = dictPattern.matcher(line);

            if (!pair.matches())
            {
                throw new IncorrectLineException("Incorrect line in dictionary : \"" + line + "\"");
            }

            dictionary.put(pair.group(1).charAt(0), pair.group(2));
        }
    }

    public void translate(Reader iStream, Writer oStream, Writer statStream) throws IllegalTokenException,
                                                                                    IOException,
                                                                                    NullPointerException
    {
        if ((iStream == null) || (oStream == null) || (statStream == null))
        {
            throw new NullPointerException("One of the arguments is null");
        }

        TreeSet<SymbolCounter> statistic = new TreeSet<SymbolCounter>();

        BufferedReader reader = new BufferedReader(iStream);
        BufferedWriter oWriter = new BufferedWriter(oStream);
        BufferedWriter statWriter = new BufferedWriter(statStream);

        String line;
        while ((line = reader.readLine()) != null)
        {   
            for (int i = 0; i < line.length(); i++)
            {
                SymbolCounter newCounter = new SymbolCounter(line.charAt(i));
                if (statistic.contains(newCounter))
                {
                    for (SymbolCounter counter : statistic)
                    {
                        if (counter.symbol() == newCounter.symbol())
                        {
                            counter.inc();
                            break;
                        }
                    }
                }
                else
                {
                    statistic.add(newCounter);
                }

                String token;
                if ( (token = dictionary.get(line.charAt(i))) != null )
                {
                    oWriter.write(token + (i == line.length() - 1 ? "" : " "));
                }
                else
                {
                    throw new IllegalTokenException("Illegal symbol : \"" + Character.toString(line.charAt(i)) + "\" at line : \"" + line + "\"");
                }
            }
            
            oWriter.newLine();
        }
        
        for (SymbolCounter counter : statistic)
        {
            statWriter.write("'" + Character.toString(counter.symbol()) + "' : " + Long.toString(counter.count()));
            statWriter.newLine();
        }

        statWriter.flush();
        oWriter.flush();
    }
}
