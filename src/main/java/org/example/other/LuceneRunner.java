package org.example.other;

import org.example.service.BrowserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LuceneRunner  {

    private final BrowserService browserService;

    public LuceneRunner(BrowserService browserService) {
        this.browserService = browserService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run() throws Exception {
        browserService.createIndex();
    }
}
