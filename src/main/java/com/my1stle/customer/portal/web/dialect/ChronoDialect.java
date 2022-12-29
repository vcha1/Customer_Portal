package com.my1stle.customer.portal.web.dialect;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * This a thymeleaf dialect for rendering time
 *
 * @implNote used the following guides
 * <a href="https://www.thymeleaf.org/doc/articles/sayhelloextendingthymeleaf5minutes.html">Say Hello! Extending Thymeleaf in 5 minutes</a>
 * <a href="https://www.thymeleaf.org/doc/articles/sayhelloagainextendingthymeleafevenmore5minutes.html">Say Hello Again! Extending Thymeleaf even more in another 5 minutes</a>"
 */
public class ChronoDialect extends AbstractProcessorDialect {

    private static final String DIALECT_NAME = "Chrono Dialect";
    private static final String DIALECT_PREFIX = "chrono";
    private static final Integer DIALECT_PREFERENCE = 1000;

    public ChronoDialect() {
        super(DIALECT_NAME, DIALECT_PREFIX, DIALECT_PREFERENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        // Add Custom tag processor here
        processors.add(new ChronoCurrentUserLocalTimeProcessor(dialectPrefix));
        processors.add(new ChronoCurrentUserDateProcessor(dialectPrefix));
        return processors;
    }
}
