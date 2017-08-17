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

    @Test
    public void mainメソッドを実行() {
        // main()メソッドを実行
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