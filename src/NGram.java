import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;


public class NGram {
		public static final double punish = -100;
		public HashMap<String,Double> dict1 = new HashMap<String, Double>();
		public HashMap<String,Double> back1 = new HashMap<String, Double>();
		public HashMap<String,Double> dict2 = new HashMap<String, Double>();
		public HashMap<String,Double> back2 = new HashMap<String, Double>();
		public HashMap<String,Double> dict3 = new HashMap<String, Double>();
		public HashMap<String,Double> back3 = new HashMap<String, Double>();
		public NGram(String modelFilePath) throws IOException {
			File filename = new File(modelFilePath);
	        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));   
	        BufferedReader br = new BufferedReader(reader);   
	        String line = "";
	        line = br.readLine();
	        int ngram1=0;
	        int ngram2=0;
	        int ngram3=0;
	        int num = 1;
            while (line != null) {
            	
            	if(num<=5&&num>=3) {
            		String [] tmp = line.split("=");
            		if (num==3) ngram1=Integer.parseInt(tmp[tmp.length-1]);
            		if (num==4) ngram2=Integer.parseInt(tmp[tmp.length-1]);
            		if (num==5) ngram3=Integer.parseInt(tmp[tmp.length-1]);
            	}
            	
            	if(num>=8&&num<=ngram1+7) {
            		String [] tmp = line.split("\t");
            		if (tmp.length==2) {
            			dict1.put(tmp[1],Double.parseDouble(tmp[0]));
            		}
            		if (tmp.length==3) {
            			dict1.put(tmp[1], Double.parseDouble(tmp[0]));
            			back1.put(tmp[1], Double.parseDouble(tmp[2]));
            		}
            	}
            	
            	if(num>=ngram1+7+3&&num<=ngram1+7+3+ngram2-1) {
            		String [] tmp = line.split("\t");
            		if (tmp.length==2) {
            			dict2.put(tmp[1],Double.parseDouble(tmp[0]));
            		}
            		if (tmp.length==3) {
            			dict2.put(tmp[1], Double.parseDouble(tmp[0]));
            			back2.put(tmp[1], Double.parseDouble(tmp[2]));
            		}
            	}
            	
            	if(num>=ngram1+7+3+ngram2-1+3&&num<=ngram1+7+3+ngram2-1+3+ngram3-1) {
            		String [] tmp = line.split("\t");
            		if (tmp.length==2) {
            			dict3.put(tmp[1],Double.parseDouble(tmp[0]));
            		}
            		if (tmp.length==3) {
            			dict3.put(tmp[1], Double.parseDouble(tmp[0]));
            			back3.put(tmp[1], Double.parseDouble(tmp[2]));
            		}
            	}
            	
            	num++;
                line = br.readLine();  
            }
            br.close();
		}
		
	public Double cal3(String s1,String s2,String s3) {
		if (s1.length()==0||s2.length()==0||s3.length()==0)
			return punish;
		
		if (dict3.containsKey(s1+" "+s2+" "+s3)) {
			return dict3.get(s1+" "+s2+" "+s3);
		}
		else
			if (back2.containsKey(s1+" "+s2))
				return back2.get(s1+" "+s2)+cal2(s2,s3);
			else
				return cal2(s2,s3);
	}
	
	public Double cal2(String s1,String s2) {
		if (s1.length()==0||s2.length()==0)
			return punish;
		
		if (dict2.containsKey(s1+" "+s2)) {
			return dict2.get(s1+" "+s2);
		}
		else {
			if (back1.containsKey(s1))
				return back1.get(s1)+cal1(s2);
			else
				return cal1(s2);
		}
	}
	
	public Double cal1(String s1) {
		if (s1.length()==0)
			return punish;
		
		if (dict1.containsKey(s1))
			return dict1.get(s1);
		else {
			//old
			//return punish;
			
			//new
			if (s1.length()>3)
				return punish*10+10*(s1.length()-3);
			else
				return punish;
		}	
	}
}
