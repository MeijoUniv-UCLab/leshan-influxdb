/*******************************************************************************
 * Copyright (c) 2024 Sierra Wireless and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.leshan.core.link.lwm2m.attributes;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Set;

import org.eclipse.leshan.core.parser.StringParser;

public class PositiveBigDecimalAttributeModel extends LwM2mAttributeModel<BigDecimal> {

    public PositiveBigDecimalAttributeModel(String coRELinkParam, Set<Attachment> attachment, AccessMode accessMode,
            AttributeClass attributeClass) {
        super(coRELinkParam, attachment, accessMode, attributeClass);
    }

    @Override
    public String toCoreLinkValue(LwM2mAttribute<BigDecimal> attr) {
        // We can not use default ToString() because we don't want to use scientific notation.
        // see more details : https://stackoverflow.com/a/25307973/5088764
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(340);
        return df.format(attr.getValue());
    }

    @Override
    public String getInvalidValueCause(BigDecimal value) {
        if (value.signum() < 0) {
            return String.format("'%s' attribute value must not be negative", getName());
        }
        return null;
    }

    /**
     * <pre>
     * 1*DIGIT ["." 1*DIGIT]
     * </pre>
     */
    @Override
    public <E extends Throwable> LwM2mAttribute<BigDecimal> consumeAttributeValue(StringParser<E> parser) throws E {
        // parse Value
        int start = parser.getPosition();
        parser.consumeDIGIT();
        while (parser.nextCharIsDIGIT()) {
            parser.consumeNextChar();
        }
        if (parser.nextCharIs('.')) {
            parser.consumeNextChar();
            parser.consumeDIGIT();
            while (parser.nextCharIsDIGIT()) {
                parser.consumeNextChar();
            }
        }
        int end = parser.getPosition();

        // create attribute
        String strValue = parser.substring(start, end);
        try {
            return new LwM2mAttribute<BigDecimal>(this, new BigDecimal(strValue));
        } catch (NumberFormatException e) {
            parser.raiseException(e, "%s value '%s' is not a valid positive double in %s", getName(), strValue,
                    parser.getStringToParse());
            return null;
        } catch (IllegalArgumentException e) {
            parser.raiseException(e, "%s value '%s' is not a valid positive double in %s", getName(), strValue,
                    parser.getStringToParse());
            return null;
        }
    }

    @Override
    public LwM2mAttribute<BigDecimal> createEmptyAttribute() {
        return new LwM2mAttribute<BigDecimal>(this);
    }
}
