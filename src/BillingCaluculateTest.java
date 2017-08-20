import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BillingCaluculateTest {

	// http://tercel-tech.hatenablog.com/entry/2015/02/28/204148 を参考

    private PrintStream defaultPrintStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() {
        defaultPrintStream = System.out;
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(new BufferedOutputStream(byteArrayOutputStream)));
    }

    // 基本料金テスト
    @Test
    public void testCalcKihonryokinKazokuwariHirutokuwari() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceKazokuwari();
    	k[0].setServiceHirutokuwari();
    	assertEquals(1300, k[0].calcKihonRyokin());
    }
    @Test
    public void testCalcKihonryokinKazokuwari() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceKazokuwari();
    	assertEquals(1100, k[0].calcKihonRyokin());
    }
    @Test
    public void testCalcKihonryokinHirutokuwari() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceHirutokuwari();
    	assertEquals(1200, k[0].calcKihonRyokin());
    }
    @Test
    public void testCalcKihonryokinServiceNashi() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	assertEquals(1000, k[0].calcKihonRyokin());
    }

    // 通話料金テスト　サービスなし
    @Test
    public void testCalcTsuuwaRyokinServiceNashi() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
       	k[0].setTsuuwa("2000/01/01", "00:00", "001", "000-000-0000");
    	assertEquals(20, k[0].calcTsuuwaRyokin());
    }

    // 通話料金テスト　昼トク割引
    @Test
    public void testCalcTsuuwaRyokinHirrutokuwariJikangai0000() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceHirutokuwari();
       	k[0].setTsuuwa("2000/01/01", "00:00", "001", "000-000-0000");
    	assertEquals(20, k[0].calcTsuuwaRyokin());
    }
    @Test
    public void testCalcTsuuwaRyokinHirrutokuwariJikangai1800() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceHirutokuwari();
       	k[0].setTsuuwa("2000/01/01", "18:00", "001", "000-000-0000");
    	assertEquals(20, k[0].calcTsuuwaRyokin());
    }
    @Test
    public void testCalcTsuuwaRyokinHirrutokuwariJikannai0800() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceHirutokuwari();
       	k[0].setTsuuwa("2000/01/01", "08:00", "001", "000-000-0000");
    	assertEquals(15, k[0].calcTsuuwaRyokin());
    }
    @Test
    public void testCalcTsuuwaRyokinHirrutokuwariJikannai0900() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceHirutokuwari();
       	k[0].setTsuuwa("2000/01/01", "09:00", "001", "000-000-0000");
    	assertEquals(15, k[0].calcTsuuwaRyokin());
    }
    @Test
    public void testCalcTsuuwaRyokinHirrutokuwariJikannai1759() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceHirutokuwari();
       	k[0].setTsuuwa("2000/01/01", "17:59", "001", "000-000-0000");
    	assertEquals(15, k[0].calcTsuuwaRyokin());
    }

    // 通話料金テスト　家族割引
    @Test
    public void testCalcTsuuwaRyokinKazokuwariNumberIcchi() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceKazokuwari();
    	k[0].setServiceKazokuwariNo("000-000-0000");
       	k[0].setTsuuwa("2000/01/01", "00:00", "001", "000-000-0000");
    	assertEquals(10, k[0].calcTsuuwaRyokin());
    }
    @Test
    public void testCalcTsuuwaRyokinKazokuwariNumberFuicchi() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceKazokuwari();
    	k[0].setServiceKazokuwariNo("000-000-0001");
       	k[0].setTsuuwa("2000/01/01", "00:00", "001", "000-000-0000");
    	assertEquals(20, k[0].calcTsuuwaRyokin());
    }

    // 通話料金テスト　家族割引&昼トク割引
    @Test
    public void testCalcTsuuwaRyokinKazoHiruJikannaiNumberIcchi() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceKazokuwari();
    	k[0].setServiceHirutokuwari();
    	k[0].setServiceKazokuwariNo("000-000-0000");
       	k[0].setTsuuwa("2000/01/01", "08:00", "001", "000-000-0000");
    	assertEquals(7, k[0].calcTsuuwaRyokin());
    }
    @Test
    public void testCalcTsuuwaRyokinKazoHiruJikannaiNumberFuicchi() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceKazokuwari();
    	k[0].setServiceHirutokuwari();
    	k[0].setServiceKazokuwariNo("000-000-0001");
       	k[0].setTsuuwa("2000/01/01", "08:00", "001", "000-000-0000");
    	assertEquals(15, k[0].calcTsuuwaRyokin());
    }
    @Test
    public void testCalcTsuuwaRyokinKazoHiruJikangaiNumberIcchi() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceKazokuwari();
    	k[0].setServiceHirutokuwari();
    	k[0].setServiceKazokuwariNo("000-000-0000");
       	k[0].setTsuuwa("2000/01/01", "00:00", "001", "000-000-0000");
    	assertEquals(10, k[0].calcTsuuwaRyokin());
    }
    @Test
    public void testCalcTsuuwaRyokinKazoHiruJikangaiNumberFuicchi() {
    	Keiyakusya k[] = new Keiyakusya[1000];
    	k[0] = new Keiyakusya();
    	k[0].setServiceKazokuwari();
    	k[0].setServiceHirutokuwari();
    	k[0].setServiceKazokuwariNo("000-000-0001");
       	k[0].setTsuuwa("2000/01/01", "00:00", "001", "000-000-0000");
    	assertEquals(20, k[0].calcTsuuwaRyokin());
    }

    // 課題の答えがあっているかの確認
    @Test
    public void testMain() {
        BillingCaluculate.main(new String[]{});

        // 標準出力の内容を取得
        System.out.flush();
        final String actual = byteArrayOutputStream.toString();

        // 期待値を設定
        final String expected = "1 090-1234-0001" + (char)0x0A
        		+ "5 1100" + (char)0x0A
        		+ "7 230" + (char)0x0A
        		+ "9 ====================" + (char)0x0A
        		+ "1 090-1234-0002" + (char)0x0A
        		+ "5 1300" + (char)0x0A
        		+ "7 199" + (char)0x0A
        		+ "9 ====================" + (char)0x0A
        		+ "1 090-1234-7777" + (char)0x0A
        		+ "5 1000" + (char)0x0A
        		+ "7 800" + (char)0x0A
        		+ "9 ====================" + System.lineSeparator();

        assertThat(actual, is(expected));
    }

    @After
    public void tearDown() {
        System.setOut(defaultPrintStream);
    }
}