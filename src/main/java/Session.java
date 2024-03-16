import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class Session {
	private HashMap<String, String> pages;
	private HashMap<String, String> options;
	private String output;
	private String pageKey;

	private HashMap<String, Function<String, String>> ifFuncs;
	
	public Session(String fileName, String opening)
	throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		pages = new HashMap<String, String>();
		ifFuncs = new HashMap<String, Function<String, String>>();
		
		String key = "";
		String val = "";

		while (true) {
			String ln = in.readLine();
			if (ln == null) {
				pages.put(key, val);
				break;
			} else if (ln.length() > 0 && ln.charAt(0) == '#') {
				pages.put(key, val);
				key = ln.substring(1);
				val = "";
			} else {
				val += ln + "\n";
			}
		}
	
		in.close();
		pageKey = opening;
	}

	public void addIfFunc(String funcName, Function<String, String> func) {
		ifFuncs.put(funcName, func);
	}

	public String getOutput() {
		return output;
	}

	public Set<String> getInputOptions() {
		return options.keySet();
	}

	public void giveInput(String input) {
		pageKey = options.get(input);
		update();
	}

	public void update() {
		String page = pages.get(pageKey);
		while (page.contains("[?")) {
			int i = page.lastIndexOf("[?");
			int colon = page.indexOf(":", i);
			int closeBracket = page.indexOf("]", i);
			int openBrace = page.indexOf("{", i);
			int closeBrace = page.indexOf("}", i);

			String output = ifFuncs.get(page.substring(i + 2, colon)).apply(page.substring(colon + 1, closeBracket));
			
			if (output == null) {
				page = page.substring(0, i) + page.substring(closeBrace + 1);
			} else {
				page = page.substring(0, i) + output + page.substring(openBrace + 1, closeBrace) + page.substring(closeBrace + 1);
			}
		}

		output = page.substring(0, page.indexOf('_'));
		String[] lines = page.substring(page.indexOf('_') + 1).split("\n");

		options = new HashMap<String, String>();
		for (String line : lines) {
			if (line.contains(";")) {
				options.put(line.substring(0, line.indexOf(';')), line.substring(line.indexOf(';') + 1));
			}
		}
	}
}