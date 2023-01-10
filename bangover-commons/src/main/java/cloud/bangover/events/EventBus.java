package cloud.bangover.events;

/**
 * This interface describes the event bus abstraction. The event bus is the component which
 * distributes domain events between different subdomains or publishes them outside (for example if
 * we are going notify another system about event inside our system).
 *
 * @author Dmitry Mikhaylenko
 *
 */
public interface EventBus {
  /**
   * Get event publisher for the specified type.
   *
   * @param <E>       The event type name
   * @param eventType The event type
   * @return The publisher
   */
  public <E> EventPublisher<E> getPublisher(EventType<E> eventType);

  /**
   * Subscribe to the event.
   *
   * @param <E>           The event type name
   * @param eventType     The type of the event
   * @param eventListener The event listener
   * @return The event subscription object
   */
  public <E> EventSubscribtion subscribeOn(EventType<E> eventType, EventListener<E> eventListener);

  /**
   * Subscribe to the every published event.
   * 
   * @param globalListener The global event listener
   * @return The event subscription object
   */
  public EventSubscribtion subscribeOnAll(GlobalEventListener globalListener);

  /**
   * This interface describes the contract of element, which receives every asynchronous domain
   * events.
   */
  public interface GlobalEventListener {
    /**
     * Receive the published event.
     *
     * @param eventDescriptor The event descriptor
     */
    void onEvent(EventDescriptor<Object> eventDescriptor);
  }

  /**
   * The event subscribtion.
   *
   * @author Dmitry Mikhaylenko
   *
   */
  public interface EventSubscribtion {
    /**
     * Unsubscribe.
     */
    public void unsubscribe();
  }

  /**
   * The event bus factory.
   *
   * @author Dmitry Mikhaylenko
   *
   */
  public interface Factory {
    /**
     * Create an event bus.
     *
     * @return The event bus
     */
    public EventBus createEventBus();
  }
}
