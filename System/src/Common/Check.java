package Common;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface Check {
	public void generate() throws FileNotFoundException;
	public boolean check()throws IOException;
}