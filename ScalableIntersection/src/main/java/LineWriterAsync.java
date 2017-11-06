import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class LineWriterAsync implements Callable<Boolean> {
	BufferedWriter writer;
	String line;
	ArrayList<String> lines = new ArrayList<String>();

	public LineWriterAsync() {
	}

	public LineWriterAsync(BufferedWriter writer) {
		this.writer = writer;
		//this.line = line;
	}

	public Boolean writeLines() {
		try {
			for (String line : lines) {
				this.writer.write(line);
				this.writer.newLine();
			}

			return true;
		} catch (IOException e) {
			return false;
		}

	}

	public void putLine(String line) {
		lines.add(line);
	}

	public int getLineCount() {
		return lines.size();
	}

	@Override
	public Boolean call() throws Exception {
		boolean foo=this.writeLines();
		//this.lines=new ArrayList<String>();
		return foo;
	}
}
