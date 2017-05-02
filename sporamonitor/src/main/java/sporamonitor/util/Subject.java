package sporamonitor.util;

import java.util.function.Consumer;

/**
 * A type-safe implementation of the subject in the observer pattern that uses Java 8 lambdas for observers.
 * @param <T> the type of the state of the subject
 */
public interface Subject<T> {
    /**
     * Registers a new observer to be notified when the subject's state changes.
     * @param observer the observer to add to this subject
     */
    void subscribe(Consumer<T> observer);

    /**
     * Detaches a previously registered observer from this subject.
     * @param observer the observer to remove from this subject
     * @throws IllegalArgumentException if the observer is not attached to the subject
     */
    void unsubscribe(Consumer<T> observer) throws IllegalArgumentException;
}
