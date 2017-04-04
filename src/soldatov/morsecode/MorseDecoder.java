package soldatov.morsecode;

import java.io.*;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import soldatov.langtranslator.*;


public class MorseDecoder implements LanguageTranslator
{
    private final TreeMap<String, Character> dictionary;
    private static final Pattern dictPattern = Pattern.compile("(.+)\\s+([\\-\\.]+)");

    public MorseDecoder(TreeMap<String, Character> dictionary)
    {
        this.dictionary = (TreeMap<String,Character>)dictionary.clone();
    }

    public MorseDecoder(Reader dictStream) throws IllegalTokenException, IOException,
                                                  NullPointerException
    {
        if (dictStream == null)
        {
            throw new NullPointerException("dictStream can't be null");
        }

        dictionary = new TreeMap<String, Character>();

        BufferedReader reader = new BufferedReader(dictStream);

        String line = null;
        while ((line = reader.readLine()) != null)
        {
            Matcher pair = dictPattern.matcher(line);

            if (!pair.matches())
            {
                throw new IncorrectLineException("Incorrect line in dictionary : \"" + line + "\"");
            }

            dictionary.put(pair.group(2), pair.group(1).charAt(0));
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

        String line = null;
        while ((line = reader.readLine()) != null)
        {
            if (line.length() > 0)
            {
                String codes[] = line.split("\\s+");

                for (String code : codes)
                {
                    Character symbol = null;
                    if ((symbol = dictionary.get(code)) != null)
                    {
                        oWriter.write(symbol);
                    }
                    else
                    {
                        throw new IllegalTokenException("Illegal code : \"" + code + "\" at line : \"" + line + "\"");
                    }

                    SymbolCounter newCounter = new SymbolCounter(symbol.charValue());
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
