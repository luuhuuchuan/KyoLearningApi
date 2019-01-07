package kyo.util;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVUtils {
private static final char DEFAULT_SEPARATOR = ',';
    
	/**
	 * write list data to file csv
	 * @param w
	 * @param values
	 * @throws IOException
	 */
    public static void writeLine(Writer w, List<String> values) throws IOException {
        writeLine(w, values, DEFAULT_SEPARATOR, ' ');
    }

    /**
     * write list data to csv fiel with separators
     * @param w
     * @param values
     * @param separators
     * @throws IOException
     */
    public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
        writeLine(w, values, separators, ' ');
    }

    
    /**
     * formate data csv
     * @param value
     * @return
     */
    private static String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    /**
     * write data to file csv
     * @param w
     * @param values
     * @param separators
     * @param customQuote
     * @throws IOException
     */
    public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

        boolean first = true;

        //default customQuote is empty

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());


    }
}
