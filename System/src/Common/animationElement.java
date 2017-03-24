package Common;
import java.util.ArrayList;

public class animationElement {
	public String name;
	public int SN;
	public String state;
	public boolean isChanged;
	
	public ArrayList<Var> variable;
	
	public int getIndex(String name){
		for(int i=0;i < variable.size();i++)
			if(variable.get(i).name.equals(name))
				return i;
		return -1;
	}
	
}