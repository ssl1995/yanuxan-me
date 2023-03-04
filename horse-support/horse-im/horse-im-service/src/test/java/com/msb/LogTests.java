package com.msb;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class LogTests {

    @Test
    public void test() {
        log.info("info");
        log.warn("warn");
        log.error("error");
        log.debug("debug");
    }

}
