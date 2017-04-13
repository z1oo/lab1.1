package soldatov;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import soldatov.langtranslator.IllegalTokenException;
import soldatov.langtranslator.IncorrectLineException;


public class CodeHandler implements CommandHandler
{
    public  class Dict extends TreeMap<java.lang.Character, java.lang.String>{

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            }

            Dict temp = (Dict) o;
            Character ch;
            for(Map.Entry<Character, String> e : temp.entrySet()){
                ch = e.getKey();
                if (this.containsKey(ch))
                    if(!(this.get(ch.toString()) == e.getValue()) )
                        return false;
                else
                    return false;
            }
            return true;
            }

    }
    private final Dict dictionary;
    private static final Pattern dictPattern = Pattern.compile("(.)\\s+([\\-\\.]+)");

    public CodeHandler() throws  IOException, NullPointerException
    {
        Reader dictStream = new FileReader("code.table");

        dictionary = new Dict();

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

    private void translate(Reader iStream, Writer oStream, Writer statStream) throws
            IOException,
            NullPointerException
    {
        dictionary.equals(dictionary);
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
    public void run(String operands[]) throws IncorrectOperandsException,IOException
    {
        if (operands.length == 0)
        {
            throw new IncorrectOperandsException("Not enough operands");
        }
        else
        {
            OutputStream oStream = null;
            Writer output = null;

            try (Reader input = new FileReader(operands[0]);
                 Writer stat = new FileWriter(operands[0] + ".stat"))
            {
                oStream = (operands.length == 1 ? System.out : new FileOutputStream(operands[1]));
                output = new OutputStreamWriter(oStream);
                translate(input, output, stat);
            }
            finally
            {
                if ((oStream != null) && (oStream != System.out))
                {
                    if (output != null)
                    {
                        output.close();
                    }
                    else
                    {
                        oStream.close();
                    }
                }
            }
        }
    }
}
