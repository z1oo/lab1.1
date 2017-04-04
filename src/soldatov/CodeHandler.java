package soldatov;

import java.io.*;
import soldatov.langtranslator.IllegalTokenException;
import soldatov.langtranslator.IncorrectLineException;
import soldatov.morsecode.MorseCoder;


public class CodeHandler implements CommandHandler
{
    public void run(String operands[]) throws IncorrectOperandsException, IllegalTokenException,
                                              IncorrectLineException, IOException
    {
        if (operands.length == 0)
        {
            throw new IncorrectOperandsException("Not enough operands");
        }
        else
        {
            OutputStream oStream = null;
            Writer output = null;

            try (Reader table = new FileReader("code.table");
                 Reader input = new FileReader(operands[0]);
                 Writer stat = new FileWriter(operands[0] + ".stat"))
            {
                oStream = (operands.length == 1 ? System.out : new FileOutputStream(operands[1]));
                output = new OutputStreamWriter(oStream);
                (new MorseCoder(table)).translate(input, output, stat);
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
