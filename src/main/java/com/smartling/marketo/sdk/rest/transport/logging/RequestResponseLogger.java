package com.smartling.marketo.sdk.rest.transport.logging;

import java.util.List;

public interface RequestResponseLogger {
    void log(String msg, List<LoggingArgument> arguments);
    boolean enabled();
}
