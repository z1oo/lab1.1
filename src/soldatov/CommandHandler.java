package soldatov;

import java.io.*;
import soldatov.langtranslator.*;


public interface CommandHandler
{
    public void run(String operands[]) throws IncorrectOperandsException, IllegalTokenException,
                                              IncorrectLineException, IOException;
}
