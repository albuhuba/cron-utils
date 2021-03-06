package com.cronutils.model.field.expression;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.commons.lang3.Validate;

import com.cronutils.model.field.value.IntegerFieldValue;
import com.cronutils.model.field.value.SpecialChar;
import com.cronutils.model.field.value.SpecialCharFieldValue;

/*
 * Copyright 2014 jmrozanec
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class On extends FieldExpression {

	private static final String W = "W";
	private static final String L = "L";

	private static final int DEFAULT_NTH_VALUE = -1;

	private IntegerFieldValue time;
	private IntegerFieldValue nth;
	private SpecialCharFieldValue specialChar;

	public On(SpecialCharFieldValue specialChar) {
		this(new IntegerFieldValue(DEFAULT_NTH_VALUE), specialChar);
	}

	public On(IntegerFieldValue time) {
		this(time, new SpecialCharFieldValue(SpecialChar.NONE));
	}

	public On(IntegerFieldValue time, SpecialCharFieldValue specialChar) {
		this(time, specialChar, new IntegerFieldValue(-1));
		checkArgument(!specialChar.getValue().equals(SpecialChar.HASH), "value missing for a#b cron expression");
	}

	public On(IntegerFieldValue time, SpecialCharFieldValue specialChar, IntegerFieldValue nth) {
		Validate.notNull(time, "time must not be null");
		Validate.notNull(specialChar, "special char must not null");
		Validate.notNull(nth, "nth value must not be null");

		this.time = time;
		this.specialChar = specialChar;
		this.nth = nth;
	}

	public IntegerFieldValue getTime() {
		return time;
	}

	public IntegerFieldValue getNth() {
		return nth;
	}

	public SpecialCharFieldValue getSpecialChar() {
		return specialChar;
	}

	@Override
	public String asString() {
		switch (specialChar.getValue()) {
		case NONE:
			return getTime().toString();
		case HASH:
			return String.format("%s#%s", getTime(), getNth());
		case W:
			return wCase();
		case L:
			return lCase();
		default:
			return specialChar.toString();
		}
	}

	private String lCase() {
		if (isDefault(getTime())) {
			return L;
		} else {
			return String.format("%sL", getTime());
		}
	}

	private String wCase() {
		if (isDefault(getTime())) {
			return W;
		} else {
			return String.format("%sW", getTime());
		}
	}

	private boolean isDefault(IntegerFieldValue fieldValue) {
		return fieldValue.getValue() == DEFAULT_NTH_VALUE;
	}
}
