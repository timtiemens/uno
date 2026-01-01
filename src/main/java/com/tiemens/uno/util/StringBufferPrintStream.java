package com.tiemens.uno.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class StringBufferPrintStream extends PrintStream {
    private ByteArrayOutputStream mybaos;

    public StringBufferPrintStream(ByteArrayOutputStream baos) throws UnsupportedEncodingException {
        super(baos, true, StandardCharsets.UTF_8.name());
        mybaos = baos;
    }

    public int size() {
        return mybaos.size();
    }

    public void clear() {
        mybaos.reset();
    }

    public static StringBufferPrintStream getPrintStream() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            StringBufferPrintStream ret = new StringBufferPrintStream(baos);
            return ret;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    private static PrintStream getPrintStream2() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Use try-with-resources for automatic closing (optional but good practice for
        // PrintStream)
        try (PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8.name())) {

            // 3. Write data to the PrintStream
//         ps.println("Hello, World!");
//          ps.print("This is some more text.");
            // Ensure all data is flushed to the underlying buffer
            // ps.flush();
            // 4. Convert the ByteArrayOutputStream content to a String
//     String resultString = baos.toString(StandardCharsets.UTF_8);
            return ps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // A static, final no-op PrintStream instance
    // or choose
    //   import org.apache.commons.io.output.NullOutputStream;
    //   PrintStream noOpPrintStream = new PrintStream(NullOutputStream.NULL_OUTPUT_STREAM);
    //   from Apache commons IO
    public static final PrintStream NO_OP_PRINTSTREAM = new PrintStream(
        // Create an anonymous inner class of OutputStream
        new OutputStream() {
            @Override
            public void write(int b) {
                // Do nothing: ignore the byte
            }
            @Override
            public void write(byte[] b) {
                // Do nothing: ignore the byte array
            }
            @Override
            public void write(byte[] b, int off, int len) {
                // Do nothing: ignore the byte array segment
            }
        }
    );
}
