/* Copyright (C) 2013 TU Dortmund
 * This file is part of LearnLib, http://www.learnlib.de/.
 * 
 * LearnLib is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 3.0 as published by the Free Software Foundation.
 * 
 * LearnLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with LearnLib; if not, see
 * <http://www.gnu.de/documents/lgpl.en.html>.
 */
package de.learnlib.filters.reuse;

/**
 * This exception will be thrown whenever some nondeterministic behavior
 * in the reuse tree is detected when inserting new queries.
 * 
 * @author Oliver Bauer <oliver.bauer@tu-dortmund.de>
 */
public class ReuseException extends IllegalArgumentException {
	private static final long serialVersionUID = 3661716306694750282L;

	public ReuseException(String string) {
		super(string);
	}
}
