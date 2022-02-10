package com.smartling.marketo.sdk.rest.transport.logging;

import java.io.IOException;
import java.io.OutputStream;

public class MultiOutputStream extends OutputStream {
    private OutputStream[] outputStreams;

    public MultiOutputStream(OutputStream... outputStreams) {
        java.util.Objects.requireNonNull(outputStreams);
        assert(outputStreams.length > 0);
        for(Object o: outputStreams) {
            java.util.Objects.requireNonNull(o);
        }
        this.outputStreams = outputStreams;
    }

    @Override
    public void write(int b) throws IOException {
        for(OutputStream os: outputStreams) {
            os.write(b);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        for(OutputStream os: outputStreams) {
            os.write(b);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        for(OutputStream os: outputStreams) {
            os.write(b, off, len);
        }
    }

    @Override
    public void flush() throws IOException {
        for(OutputStream os: outputStreams) {
            os.flush();
        }
    }

    @Override
    public void close() throws IOException {
        for(OutputStream os: outputStreams) {
            os.close();
        }
    }
}
