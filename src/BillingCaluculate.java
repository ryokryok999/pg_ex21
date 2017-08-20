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

	public static final String INPUT_FILE = "record.log";
	public static final String OUTPUT_FILE = "invoice.dat";

	public static final char INPUT_FIRST_CHAR_KEIYAKUSYA = '1';
	public static final char INPUT_FIRST_CHAR_KANYU_SERVICE = '2';
	public static final char INPUT_FIRST_CHAR_TSUUWA_KIROKU = '5';
	public static final char INPUT_FIRST_CHAR_KUGIRI = '9';

	public static final String SERVICE_KAZOKUWARI = "C1";
	public static final String SERVICE_HIRUTOKUWARI = "E1";

	public static void main(String[] args) {
		try{
			Keiyakusya k[] = new Keiyakusya[MAX_KEIYAKUSYA_NUM];
			File inputFile = new File(INPUT_FILE);
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line = br.readLine();

			while (line != null) {
				char firstChar = line.charAt(0);
				String[] lineWords = line.split("[\\s]+");

				switch (firstChar){
				case INPUT_FIRST_CHAR_KEIYAKUSYA:
					k[Keiyakusya.getCount()] = new Keiyakusya();
					k[Keiyakusya.getCount()-1].setKeiyakuNo(lineWords[1]);
					break;
				case INPUT_FIRST_CHAR_KANYU_SERVICE:
					switch (lineWords[1]){
					case SERVICE_KAZOKUWARI:
						k[Keiyakusya.getCount()-1].setServiceKazokuwari();
						k[Keiyakusya.getCount()-1].setServiceKazokuwariNo(lineWords[2]);
						break;
					case SERVICE_HIRUTOKUWARI:
						k[Keiyakusya.getCount()-1].setServiceHirutokuwari();
						break;
					}
					break;
				case INPUT_FIRST_CHAR_TSUUWA_KIROKU:
					k[Keiyakusya.getCount()-1].setTsuuwa(lineWords[1], lineWords[2], lineWords[3], lineWords[4]);
					break;
				case INPUT_FIRST_CHAR_KUGIRI:
					k[Keiyakusya.getCount()-1].calcKihonRyokin();
					k[Keiyakusya.getCount()-1].calcTsuuwaRyokin();
					break;
				}

				line = br.readLine();
			}
			br.close();

			try{
				File outputFile = new File(OUTPUT_FILE);
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));

				for (int i = 0; i < Keiyakusya.getCount(); i++){
					System.out.println(k[i]); //テストのため、本来はいらない
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
	public static final int MAX_SERVICE_KAZOKUWARI_NO = 2;
	public static final int MAX_TSUUWA = 1000; // 適当に1000

	public static final int KIHON_RYOKIN = 1000;
	public static final int KIHON_RYOKIN_SERVICE_KAZOKUWARI = 100;
	public static final int KIHON_RYOKIN_SERVICE_HIRUTOKUWARI = 200;
	public static final int TANI_RYOKIN = 20;

	public static final int TANI_RYOKIN_WARIBIKI_SERVICE_HIRUTOKUWARI = 5;
	public static final double TANI_RYOKIN_WARIBIKIRITSU_SERVICE_HIRUTOKUWARI = 0.5;

	public static final int START_HOUR_SERVICE_HIRUTOKUWARI = 8;
	public static final int START_MIN_SERVICE_HIRUTOKUWARI = 0;
	public static final int END_HOUR_SERVICE_HIRUTOKUWARI = 17;
	public static final int END_MIN_SERVICE_HIRUTOKUWARI = 59;

	public static final char OUTPUT_FIRST_CHAR_KEIYAKUSYA = '1';
	public static final char OUTPUT_FIRST_CHAR_KINHON_RYOKIN = '5';
	public static final char OUTPUT_FIRST_CHAR_TSUUWA_RYOKIN = '7';
	public static final char OUTPUT_FIRST_CHAR_KUGIRI = '9';
	public static final String OUTPUT_KUGIRI = "====================";

	private static int count = 0;

	private String keiyakuNo;
	private boolean serviceKazokuwari;
	private boolean serviceHirutokuwari;
	private String[] serviceKazokuwariNo = new String[MAX_SERVICE_KAZOKUWARI_NO];
	private int serviceKazokuwariNoIndex = 0;
	private String[][] tsuuwa = new String[MAX_TSUUWA][5];

	private int tsuuwaIndex = 0;
	private int kihonRyokin;
	private int tsuuwaRyokin = 0;
	private int taniRyokin;

	Keiyakusya(){
		count++;
	}

	public int calcKihonRyokin(){
		kihonRyokin = KIHON_RYOKIN;
		if(serviceKazokuwari){
			kihonRyokin += KIHON_RYOKIN_SERVICE_KAZOKUWARI;
		}
		if(serviceHirutokuwari){
			kihonRyokin += KIHON_RYOKIN_SERVICE_HIRUTOKUWARI;
		}
		return kihonRyokin;
	}

	public int calcTsuuwaRyokin(){
		for (int i = 0; i < tsuuwaIndex; i++){
			taniRyokin = TANI_RYOKIN;
			int minutes = Integer.parseInt(tsuuwa[i][2]);

			if(serviceHirutokuwari){
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
				calBegin.set(year,month,day,START_HOUR_SERVICE_HIRUTOKUWARI,START_MIN_SERVICE_HIRUTOKUWARI);
				Calendar calEnd = Calendar.getInstance();
				calEnd.set(year,month,day,END_HOUR_SERVICE_HIRUTOKUWARI,END_MIN_SERVICE_HIRUTOKUWARI);

				if ((cal.compareTo(calBegin) == 1 || cal.compareTo(calBegin) == 0) &&
						(cal.compareTo(calEnd) == -1 || cal.compareTo(calEnd) == 0)){
					taniRyokin -= TANI_RYOKIN_WARIBIKI_SERVICE_HIRUTOKUWARI;
				}
			}

			if(serviceKazokuwari){
				for (int j = 0; j < serviceKazokuwariNoIndex; j++){
					if(tsuuwa[i][3].equals(serviceKazokuwariNo[j])){
						taniRyokin *= TANI_RYOKIN_WARIBIKIRITSU_SERVICE_HIRUTOKUWARI;
					}
				}
			}
			tsuuwaRyokin += taniRyokin * minutes;
		}
		return tsuuwaRyokin;
	}

	// ここの多次元配列はイケてない。。。
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

	public void setServiceKazokuwari(){
		serviceKazokuwari = true;
	}

	public void setServiceHirutokuwari(){
		serviceHirutokuwari = true;
	}

	public void setServiceKazokuwariNo(String newServiceKazokuwariNo){
		serviceKazokuwariNo[serviceKazokuwariNoIndex] = newServiceKazokuwariNo;
		serviceKazokuwariNoIndex++;
	}

	public String toString(){

		return OUTPUT_FIRST_CHAR_KEIYAKUSYA + " " + keiyakuNo + (char)0x0A
				+ OUTPUT_FIRST_CHAR_KINHON_RYOKIN + " " + kihonRyokin + (char)0x0A
				+ OUTPUT_FIRST_CHAR_TSUUWA_RYOKIN + " " + tsuuwaRyokin + (char)0x0A
				+ OUTPUT_FIRST_CHAR_KUGIRI + " " + OUTPUT_KUGIRI;
	}

}