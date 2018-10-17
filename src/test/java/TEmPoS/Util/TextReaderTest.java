package TEmPoS.Util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TextReaderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReader() throws IOException {
        ArrayList<String> ipList = TextReader.getValidIpList();

        System.out.println(ipList);

    }
}