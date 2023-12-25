package site.pointman.chatbot.filter;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Scanner;


public class HttpServletRequestWrapper extends ServletRequestWrapper {


    private String requestData = null;

    public HttpServletRequestWrapper(HttpServletRequest request) {

        super(request);

        try (Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A")) {

            requestData = s.hasNext() ? s.next() : "";

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        StringReader reader = new StringReader(requestData);

        return new ServletInputStream() {

            private ReadListener readListener = null;

            @Override
            public int read() throws IOException {

                return reader.read();
            }

            @Override
            public void setReadListener(ReadListener listener) {
                this.readListener = listener;

                try {
                    if (!isFinished()) {

                        readListener.onDataAvailable();
                    } else {

                        readListener.onAllDataRead();
                    }
                } catch (IOException io) {

                    io.printStackTrace();
                }

            }

            @Override
            public boolean isReady() {

                return isFinished();
            }

            @Override
            public boolean isFinished() {

                try {
                    return reader.read() < 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;

            }
        };
    }
}
