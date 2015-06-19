/*
 * The MIT License
 *
 * Copyright 2015 sdymi_000.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package pl.exsio.ca.util;

import com.vaadin.data.Validator;
import com.vaadin.ui.Field;
import pl.exsio.jin.annotation.TranslationPrefix;
import static pl.exsio.jin.translationcontext.TranslationContext.t;

/**
 *
 * @author sdymi_000
 */
@TranslationPrefix("ca.tr_assignments")
public class XorValidator implements Validator {

    private final Field otherField;

    public XorValidator(Field otherField) {
        this.otherField = otherField;
    }
    
    @Override
    public void validate(Object value) throws InvalidValueException {
        if((otherField.getValue() == null && value == null) ||
                (otherField.getValue() != null && value != null)) {
            throw new Validator.InvalidValueException(t("group_or_preacher"));
        }
    }
    
}
