/* Copyright (C) 2013-2020 TU Dortmund
 * This file is part of LearnLib, http://www.learnlib.de/.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.learnlib.examples.mealy;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import de.learnlib.examples.LearningExample.StateLocalInputMealyLearningExample;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.automata.transducers.StateLocalInputMealyMachine;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.commons.util.mappings.Mapping;
import net.automatalib.commons.util.mappings.MutableMapping;
import net.automatalib.commons.util.random.RandomUtil;
import net.automatalib.util.automata.random.RandomAutomata;
import net.automatalib.words.Alphabet;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ExampleRandomStateLocalInputMealy<I, O> implements StateLocalInputMealyLearningExample<I, O> {

    private final Alphabet<I> alphabet;
    private final StateLocalInputMealyMachine<?, I, ?, O> referenceAutomaton;
    private final O undefinedOutput;

    @SafeVarargs
    public ExampleRandomStateLocalInputMealy(Alphabet<I> alphabet, int size, O undefinedOutput, O... outputs) {
        this(new Random(), alphabet, size, undefinedOutput, outputs);
    }

    @SafeVarargs
    public ExampleRandomStateLocalInputMealy(Random random,
                                             Alphabet<I> alphabet,
                                             int size,
                                             O undefinedOutput,
                                             O... outputs) {
        if (Arrays.asList(outputs).contains(undefinedOutput)) {
            throw new IllegalArgumentException("The special undefined input should not be contained in regular outputs");
        }

        this.alphabet = alphabet;
        this.undefinedOutput = undefinedOutput;
        CompactMealy<I, O> source = RandomAutomata.randomDeterministic(random,
                                                                       size,
                                                                       alphabet,
                                                                       Collections.emptyList(),
                                                                       Arrays.asList(outputs),
                                                                       new CompactMealy<>(alphabet));

        final int alphabetSize = alphabet.size();

        final Collection<Integer> oldStates = source.getStates();
        final Integer sink = source.addState();
        final MutableMapping<Integer, Collection<I>> enabledInputs = source.createDynamicStateMapping();

        for (final Integer s : oldStates) {

            final Set<I> stateInputs = new HashSet<>(alphabet);
            // randomly remove (redirect to sink) transitions
            for (int idx : RandomUtil.distinctIntegers(random.nextInt(alphabetSize), alphabetSize, random)) {
                final I sym = alphabet.getSymbol(idx);
                stateInputs.remove(sym);
                source.setTransition(s, sym, sink, undefinedOutput);
            }
            enabledInputs.put(s, stateInputs);
        }

        // configure sink
        for (final I i : alphabet) {
            source.addTransition(sink, i, sink, undefinedOutput);
        }
        enabledInputs.put(sink, Collections.emptyList());

        this.referenceAutomaton = new MockedSLIMealy<>(source, enabledInputs);
    }

    @SafeVarargs
    public static <I, O> ExampleRandomStateLocalInputMealy<I, O> createExample(Random random,
                                                                               Alphabet<I> alphabet,
                                                                               int size,
                                                                               O undefinedOutput,
                                                                               O... outputs) {
        return new ExampleRandomStateLocalInputMealy<>(random, alphabet, size, undefinedOutput, outputs);
    }

    @Override
    public StateLocalInputMealyMachine<?, I, ?, O> getReferenceAutomaton() {
        return referenceAutomaton;
    }

    @Override
    public Alphabet<I> getAlphabet() {
        return alphabet;
    }

    @Override
    public O getUndefinedOutput() {
        return this.undefinedOutput;
    }

    private static class MockedSLIMealy<S, I, T, O> implements StateLocalInputMealyMachine<S, I, T, O> {

        private final MealyMachine<S, I, T, O> delegate;
        private final Mapping<S, Collection<I>> localInputs;

        MockedSLIMealy(MealyMachine<S, I, T, O> delegate, Mapping<S, Collection<I>> localInputs) {
            this.delegate = delegate;
            this.localInputs = localInputs;
        }

        @Override
        public Collection<I> getLocalInputs(S state) {
            return this.localInputs.get(state);
        }

        @Override
        public Collection<S> getStates() {
            return this.delegate.getStates();
        }

        @Override
        public O getTransitionOutput(T transition) {
            return this.delegate.getTransitionOutput(transition);
        }

        @Nullable
        @Override
        public T getTransition(S state, I input) {
            return this.delegate.getTransition(state, input);
        }

        @Override
        public S getSuccessor(T transition) {
            return this.delegate.getSuccessor(transition);
        }

        @Nullable
        @Override
        public S getInitialState() {
            return this.delegate.getInitialState();
        }
    }
}
