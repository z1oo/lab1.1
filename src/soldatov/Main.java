package soldatov;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import soldatov.langtranslator.*;


public class Main
{
    private static final HashMap<String, CommandHandler> commands;

    static {
        commands = new HashMap<String, CommandHandler>();
        try {
            commands.put("code", new CodeHandler());
            commands.put("decode", new DecodeHandler());
            commands.put("exit", new ExitHandler());
        } catch (IOException e) {
            System.out.println("I\\O error : " + e.getMessage());
        }

    }

    public static void main(String[] args)
    {
        try (BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in)))
        {
            String line = null;
            while ((line = stdin.readLine()) != null)
            {
                line = line.toLowerCase();

                try
                {
                    String command[] = line.split("\\s+");

                    CommandHandler handler = commands.get(command[0]);
                    if (handler == null)
                    {
                        throw new IncorrectOperandsException("Incorrect command. Try again");
                    }

                    handler.run(Arrays.copyOfRange(command, 1, command.length));
                    System.out.println("Done.");
                }
                catch (BreakException e)
                {
                    break;
                }
                catch (IncorrectOperandsException e)
                {
                    System.out.println("Error : \"" + line + "\" : " + e.getMessage() + "\n");
                }
                catch ( IllegalTokenException | IncorrectLineException e)
                {
                    System.out.println("Error : \"" + line + "\" : " + e.getMessage());
                }
                catch (IOException e)
                {
                    System.out.println("I\\O error : " + e.getMessage());
                }

                System.out.println();
            }
        }
        catch (Throwable err)
        {
            System.err.println("Unexpected error: " + err.getMessage());
        }
    }
}

