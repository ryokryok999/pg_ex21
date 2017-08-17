import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class BillingCaluculate {

	public static final int MAX_KEIYAKUSYA_NUM = 1000; // 適当に1000

	public static void main(String[] args) {
		try{
			Keiyakusya k[] = new Keiyakusya[MAX_KEIYAKUSYA_NUM];
			File inputFile = new File("record.log");
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line = br.readLine();

			while (line != null) {
				char firstChar = line.charAt(0);
				String[] lineWords = line.split("[\\s]+");

				switch (firstChar){
				case '1':
					k[Keiyakusya.getCount()] = new Keiyakusya();
					k[Keiyakusya.getCount()-1].setKeiyakuNo(lineWords[1]);
					break;
				case '2':
					switch (lineWords[1]){
					case "C1":
						k[Keiyakusya.getCount()-1].setC1();
						k[Keiyakusya.getCount()-1].setC1No(lineWords[2]);
						break;
					case "E1":
						k[Keiyakusya.getCount()-1].setE1();
						break;
					}
					break;
				case '5':
					k[Keiyakusya.getCount()-1].setTsuuwa(lineWords[1], lineWords[2], lineWords[3], lineWords[4]);
					break;
				case '9':
					k[Keiyakusya.getCount()-1].calcKihonRyokin();
					k[Keiyakusya.getCount()-1].calcTsuuwaRyokin();
					break;
				}

				line = br.readLine();
			}
			br.close();

			try{
				File outputFile = new File("invoice.dat");
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));

				for (int i = 0; i < Keiyakusya.getCount(); i++){
					System.out.println(k[i]); //本来はいらない
					pw.println(k[i]);
				}
				pw.close();
			}catch(IOException e) {
				System.out.println(e);
			}

		}catch(FileNotFoundException e) {
			System.out.println(e);
		}catch(IOException e) {
			System.out.println(e);
		}
	}
}

class Keiyakusya {
	private static int count = 0;
	private String keiyakuNo;
	private boolean c1;
	private boolean e1;
	private String[] c1No = new String[2];
	private int c1NoIndex = 0;
	private String[][] tsuuwa = new String[1000][5];
	private int tsuuwaIndex = 0;
	private int kihonRyokin = 0;
	private int tsuuwaRyokin = 0;
	private int taniRyokin;

	Keiyakusya(){
		count++;
	}

	public void calcKihonRyokin(){
		kihonRyokin += 1000;
		if(c1){
			kihonRyokin += 100;
		}
		if(e1){
			kihonRyokin += 200;
		}
	}

	public void calcTsuuwaRyokin(){


		for (int i = 0; i < tsuuwaIndex; i++){
			taniRyokin = 20;
			int time = Integer.parseInt(tsuuwa[i][2]);

			if(e1){
				//System.out.println("tsuuwa["+i+"][3]=" + tsuuwa[i][3]);
				//System.out.println("c1No["+j+"]=" + c1No[j]);
				String[] yyyymmdd = tsuuwa[i][0].split("/");
				String[] hhmm = tsuuwa[i][1].split(":");
				int year = Integer.parseInt(yyyymmdd[0]);
				int month = Integer.parseInt(yyyymmdd[1]);
				int day = Integer.parseInt(yyyymmdd[2]);
				int hour = Integer.parseInt(hhmm[0]);
				int minute = Integer.parseInt(hhmm[1]);
				Calendar cal = Calendar.getInstance();
				cal.set(year,month,day,hour,minute);
				Calendar calBegin = Calendar.getInstance();
				calBegin.set(year,month,day,8,0);
				Calendar calEnd = Calendar.getInstance();
				calEnd.set(year,month,day,17,59);

				/*
				System.out.println("#########");
				System.out.println(year);
				System.out.println(month);
				System.out.println(day);
				System.out.println(hour);
				System.out.println(minute);

				System.out.println(cal);
				System.out.println(calBegin);
				System.out.println(calEnd);
				System.out.println(cal.compareTo(calBegin));
				System.out.println(cal.compareTo(calEnd));
				*/

				if ((cal.compareTo(calBegin) == 1 || cal.compareTo(calBegin) == 0) &&
						(cal.compareTo(calEnd) == -1 || cal.compareTo(calEnd) == 0)){
					taniRyokin -= 5;
					//System.out.println(taniRyokin);
				}
			}

			if(c1){
				//System.out.println("c1マッチ！");
				for (int j = 0; j < c1NoIndex; j++){
					if(tsuuwa[i][3].equals(c1No[j])){
						taniRyokin /= 2;
					}
				}
			}

			tsuuwaRyokin += taniRyokin * time;

			//System.out.println("====");
			//System.out.println(taniRyokin);
			//System.out.println(time);
		}
		//System.out.println("**********************");
	}

	public void setTsuuwa(String date, String time, String minutes, String num){
		tsuuwa[tsuuwaIndex][0] = date;
		tsuuwa[tsuuwaIndex][1] = time;
		tsuuwa[tsuuwaIndex][2] = minutes;
		tsuuwa[tsuuwaIndex][3] = num;
		tsuuwaIndex++;
	}

	public static int getCount(){
		return count;
	}

	public void setKeiyakuNo(String newKeiyakuNo){
		keiyakuNo = newKeiyakuNo;
	}

	public void setC1(){
		c1 = true;
	}

	public void setE1(){
		e1 = true;
	}

	public void setC1No(String newC1No){
		c1No[c1NoIndex] = newC1No;
		c1NoIndex++;
	}

	public String toString(){

		return "1 " + keiyakuNo + (char)0x0A
				+ "5 " + kihonRyokin + (char)0x0A
				+ "7 " + tsuuwaRyokin + (char)0x0A
				+ "9 ====================";

		/*
		for (int i = 0; i < tsuuwaIndex; i++){
			System.out.println(tsuuwa[i][0]);
			System.out.println(tsuuwa[i][1]);
			System.out.println(tsuuwa[i][2]);
			System.out.println(tsuuwa[i][3]);
		}

		return "契約番号=" + keiyakuNo
				+ " C1=" + c1
				+ " C1No[0]=" + c1No[0]
				+ " C1No[1]=" + c1No[1]
				+ " E1=" + e1
				+ " 基本料金=" + kihonRyokin
				+ " 通話料金=" + tsuuwaRyokin;
		*/
	}

}






