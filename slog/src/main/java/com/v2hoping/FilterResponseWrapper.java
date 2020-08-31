package com.v2hoping;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * Created by houping wang on 2020/8/20
 *
 * @author houping wang
 */
public class FilterResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream buffer = null;
    private ServletOutputStream out = null;
    private PrintWriter writer = null;

    public FilterResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        buffer = new ByteArrayOutputStream();
        out = new OutputStreamWrapper(buffer);
        writer = new PrintWriter(new OutputStreamWriter(buffer, "UTF-8"));
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return out;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (out != null) {
            out.flush();
        }
        if (writer != null) {
            writer.flush();
        }
    }

    public void outputBuffer() {
        ServletResponse response = this.getResponse();
        if(response == null) {
            return;
        }
        try {
            byte[] bytes = buffer.toByteArray();
            this.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            this.flushBuffer();
        } catch (IOException e) {
        }
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    public String getResponseData(String charset) {
        byte[] bytes = buffer.toByteArray();
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public String getResponseData() {
        byte[] bytes = buffer.toByteArray();
        try {
            return new String(bytes);
        } catch (Exception e) {
            return "";
        }
    }

    class OutputStreamWrapper extends ServletOutputStream {
        private ByteArrayOutputStream bos = null;

        public OutputStreamWrapper(ByteArrayOutputStream stream) throws IOException {
            bos = stream;
        }

        @Override
        public void write(int b) throws IOException {
            bos.write(b);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }
    }
}
