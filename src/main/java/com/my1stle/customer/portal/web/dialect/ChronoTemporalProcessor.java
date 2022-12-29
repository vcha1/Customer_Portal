package com.my1stle.customer.portal.web.dialect;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

import java.time.temporal.TemporalAccessor;

abstract class ChronoTemporalProcessor<T extends TemporalAccessor> extends AbstractAttributeTagProcessor {

    private static final int PRECEDENCE = 10000;

    ChronoTemporalProcessor(final String dialectPrefix, final String attributeName) {
        super(
                TemplateMode.HTML,
                dialectPrefix,
                null,
                false,
                attributeName,
                true,
                PRECEDENCE,
                true);
    }

    protected abstract String format(T temporal);

    @Override
    protected void doProcess(
            ITemplateContext context,
            IProcessableElementTag tag,
            AttributeName attributeName,
            String attributeValue,
            IElementTagStructureHandler structureHandler) {

        /*
         * In order to evaluate the attribute value as a Thymeleaf Standard Expression,
         * we first obtain the parser, then use it for parsing the attribute value into
         * an expression object, and finally execute this expression object.
         */
        final IEngineConfiguration configuration = context.getConfiguration();

        final IStandardExpressionParser parser =
                StandardExpressions.getExpressionParser(configuration);

        final IStandardExpression expression = parser.parseExpression(context, attributeValue);

        final T temporal = (T) expression.execute(context);

        structureHandler.setBody(HtmlEscape.escapeHtml5(temporal == null ? "N/A" : format(temporal)), false);

    }

}
