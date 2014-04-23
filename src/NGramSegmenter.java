import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;


public class NGramSegmenter {

	public static NGram g;
	
	public static void readNGram() throws IOException {
		g = new NGram("/home/zc/workspace/Segment/zuoye2/3grams.arpa");
	}
	
	public static final double punish = -10000000;
	
	public static String [] segment(String s) {
		
		int n = s.length();
		
		double [][][] v = new double [n+1][n+1][n+1];
		String [][][] p = new String [n+1][n+1][n+1];
		
		int i,j,k,h;
		
		for (j=2;j<=n;j++)
			for (i=1;i<j;i++) {
				v[0][i][j] = g.cal1("<s>")+g.cal2("<s>", s.substring(0,i))+g.cal3("<s>", s.substring(0,i), s.substring(i,j));
				p[0][i][j] = s.substring(0,i) + " " + s.substring(i,j);
				
			}
		//println("#####");
		for (k=3;k<=n;k++)
			for (j=2;j<k;j++)
				for (i=1;i<j;i++) {
					v[i][j][k] = punish;
					for (h=0;h<i;h++) {
						
						if (v[i][j][k]<v[h][i][j]+g.cal3(s.substring(h,i), s.substring(i,j), s.substring(j,k))) {
							v[i][j][k] = v[h][i][j]+g.cal3(s.substring(h,i), s.substring(i,j), s.substring(j,k));
							p[i][j][k] = p[h][i][j]+" "+s.substring(j,k);
							
						}
					}
				}
		//println("=====");
		double maxv = v[0][1][n]+g.cal3(s.substring(0,1), s.substring(1,n), "</s>");
		int ri=0,rj=1;
		for (j=1;j<=n-1;j++)
			for (i=0;i<j;i++) {
				
				if (maxv<v[i][j][n]+g.cal3(s.substring(i,j),s.substring(j,n),"</s>")) {
					
					maxv = v[i][j][n]+g.cal3(s.substring(i,j),s.substring(j,n),"</s>");
					ri=i;
					rj=j;
					
				}
			}
		//System.out.println(p[ri][rj][n]);
		return p[ri][rj][n].split(" ");
	}
	
	//tmp
	public static void print(Object o) {
		System.out.print(o);
	}
	public static void println(Object o) {
		System.out.println(o);
	}
	
	public static void main(String[] args) throws IOException {
		readNGram();
//		PrintStream ps = new PrintStream(new FileOutputStream("testout"));
//		System.setOut(ps);
		//String [] result = segment("展现魅力滨海");
		//String [] result = segment("高女士说自己五十六岁是名退休医生她想不通自己的年龄应该和男子母亲的岁数差不多了男子怎么能说动手就动手呢");
		
		print("abc\n".trim());
		
		File filename = new File("/home/zc/workspace/Segment/zuoye2/test.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));   
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int linecount=1;
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File("/home/zc/workspace/Segment/zuoye2/testout.txt")));   
        BufferedWriter bw = new BufferedWriter(writer); 
		while(line!=null) {
			String output= "";
			print(linecount);
			//print(": "+line.trim().contains("\n")+" ");
			//println(line.trim());
			String [] result = segment(line.trim());
			for (int i=0;i<result.length;i++) {
				output += result[i]+" ";
			}
			println(output.substring(0, output.length()-1));
			bw.write(output+"\n");
			line=br.readLine();
			linecount++;
		}
		br.close();
		bw.close();
		print("\n");
	}

}
