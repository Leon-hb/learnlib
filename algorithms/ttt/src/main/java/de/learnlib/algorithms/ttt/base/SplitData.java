/* Copyright (C) 2014 TU Dortmund
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
package de.learnlib.algorithms.ttt.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Data associated with a {@link DTNode} while an enclosing subtree is being split.
 * 
 * @author Malte Isberner
 *
 * @param <I> input symbol type
 */
public class SplitData<I,D> {
	
	// TODO: HashSets/Maps are quite an overkill for booleans
	private final Set<D> marks = new HashSet<>();
	private final Map<D, IncomingList<I,D>> incomingTransitions
		= new HashMap<>();
	
	private D stateLabel;
	
	/**
	 * Mark this node with the given label. The result indicates whether
	 * it has been newly marked.
	 * 
	 * @param label the label to mark this node with
	 * @return {@code true} if the node was previously unmarked (wrt. to the given label),
	 * {@code false} otherwise
	 */
	public boolean mark(D label) {
		return marks.add(label);
	}
	
	public Set<D> getLabels() {
		return marks;
	}
	
	/**
	 * Checks whether there is a state label associated with this node,
	 * regardless of its value.
	 * 
	 * @return {@code true} if there is a state label ({@code true} or {@code false})
	 * associated with this node, {@code false} otherwise
	 */
	public boolean hasStateLabel() {
		return (stateLabel != null);
	}
	
	/**
	 * Sets the state label associated with this split data.
	 * <p>
	 * <b>Note:</b> invoking this operation is illegal if a state label has already
	 * been set.
	 * 
	 * @param label the state label
	 */
	public void setStateLabel(D label) {
		assert !hasStateLabel();
		
		this.stateLabel = label;
	}
	
	/**
	 * Retrieves the state label associated with this split data.
	 * <p>
	 * <b>Note:</b> invoking this operation is illegal if no state label
	 * has previously been set.
	 * @return the state label
	 */
	public D getStateLabel() {
		assert hasStateLabel();
		
		return stateLabel;
	}
	
	/**
	 * Retrieves the list of incoming transitions for the respective label.
	 * <p>
	 * This method will always return a non-{@code null} value.
	 * 
	 * @param label the label
	 * @return the (possibly empty) list associated with the given state label
	 */
	public IncomingList<I,D> getIncoming(D label) {
		IncomingList<I,D> list = incomingTransitions.get(label);
		if(list == null) {
			list = new IncomingList<>();
			incomingTransitions.put(label, list);
		}
		
		return list;
	}
	
	/**
	 * Checks whether the corresponding node is marked with the given label.
	 * 
	 * @param label the label
	 * @return {@code true} if the corresponding node is marked with the given
	 * label, {@code false} otherwise
	 */
	public boolean isMarked(D label) {
		return marks.contains(label);
	}
	
	
}