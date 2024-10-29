package cloud.bangover.events;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Value;

public class LocalEventBus implements EventBus {
  private final GlobalEventListeners globalSubscriptions = new GlobalEventListeners();
  private final Map<EventId, EventListeners> subscriptions = new HashMap<>();

  public LocalEventBus() {
    super();
    globalSubscriptions.subscribe(new LocalBusEventPublisher());
  }

  @Override
  public <E> EventPublisher<E> getPublisher(EventType<E> eventType) {
    return new EventPublisher<E>() {
      @Override
      public void publish(E event) {
        globalSubscriptions.publishEvent(eventType, event);
      }
    };
  }

  @Override
  public synchronized <E> EventSubscribtion subscribeOn(
      EventType<E> eventType, EventListener<E> eventListener) {
    EventId eventId = new EventId(eventType);
    EventListeners listeners = getListeners(eventId);
    subscriptions.put(eventId, listeners.subscribe(eventListener));
    return new EventSubscribtion() {
      @Override
      public void unsubscribe() {
        subscriptions.put(eventId, listeners.unsubscribe(eventListener));
      }
    };
  }

  @Override
  public EventSubscribtion subscribeOnAll(GlobalEventListener globalListener) {
    globalSubscriptions.subscribe(globalListener);
    return new EventSubscribtion() {
      @Override
      public void unsubscribe() {
        globalSubscriptions.unsubscribe(globalListener);
      }
    };
  }

  private EventListeners getListeners(EventId eventId) {
    return subscriptions.getOrDefault(eventId, new EventListeners());
  }

  @Value
  private class EventId {
    private EventType<?> eventType;
  }

  private class LocalBusEventPublisher implements GlobalEventListener {
    @Override
    public void onEvent(EventDescriptor<Object> eventDescriptor) {
      EventId eventId = new EventId(eventDescriptor.getEventType());
      EventListeners eventListeners = getListeners(eventId);
      eventListeners.publishEvent(eventDescriptor.getEvent());
    }
  }

  private class GlobalEventListeners {
    private final Set<GlobalEventListener> listeners = new HashSet<>();

    @SuppressWarnings("unchecked")
    public <E> void publishEvent(EventType<E> type, E event) {
      EventDescriptor<Object> eventDescriptor =
          new EventDescriptor<Object>((EventType<Object>) type, event);
      this.listeners.forEach(listener -> listener.onEvent(eventDescriptor));
    }

    public GlobalEventListeners subscribe(GlobalEventListener listener) {
      this.listeners.add(listener);
      return this;
    }

    public GlobalEventListeners unsubscribe(GlobalEventListener listener) {
      this.listeners.remove(listener);
      return this;
    }
  }

  private class EventListeners {
    private final Collection<EventListener<?>> listeners = new HashSet<>();

    @SuppressWarnings("unchecked")
    public <E> void publishEvent(E event) {
      listeners.forEach(listener -> ((EventListener<Object>) listener).onEvent(event));
    }

    public <E> EventListeners subscribe(EventListener<E> listener) {
      this.listeners.add(listener);
      return this;
    }

    public <E> EventListeners unsubscribe(EventListener<E> listener) {
      this.listeners.remove(listener);
      return this;
    }
  }
}
