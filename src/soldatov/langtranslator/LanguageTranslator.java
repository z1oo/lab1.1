package soldatov.langtranslator;

import java.io.Reader;
import java.io.Writer;
import java.io.IOException;


public interface LanguageTranslator
{
    public void translate(Reader iStream, Writer oStream, Writer statStream) throws IllegalTokenException,
                                                                                    IOException,
                                                                                    NullPointerException;
}
