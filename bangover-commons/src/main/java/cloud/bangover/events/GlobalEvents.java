package cloud.bangover.events;

import cloud.bangover.events.EventBus.EventSubscribtion;
import cloud.bangover.events.EventBus.GlobalEventListener;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GlobalEvents {
  private EventBus eventBus;

  static {
    SystemEvents.systemEventsBus().subscribeOnAll((eventDescriptor) -> {
      eventBus.getPublisher(eventDescriptor.getEventType()).publish(eventDescriptor.getEvent());
    });
    reset();
  }

  public void configureEventBus(EventBus eventBus) {
    GlobalEvents.eventBus = eventBus;
  }

  public <E> EventPublisher<E> publisher(EventType<E> eventType) {
    return SystemEvents.publisher(eventType);
  }

  public <E> EventSubscribtion subscribeOn(EventType<E> eventType, EventListener<E> eventListener) {
    return eventBus.subscribeOn(eventType, eventListener);
  }

  public EventSubscribtion subscribeOnAll(GlobalEventListener listener) {
    return eventBus.subscribeOnAll(listener);
  }

  public void reset() {
    GlobalEvents.eventBus = new NullEventBus();
  }

  private class NullEventBus implements EventBus {
    @Override
    public <E> EventPublisher<E> getPublisher(EventType<E> eventType) {
      return new NullEventPublisher<>();
    }

    @Override
    public <E> EventSubscribtion subscribeOn(EventType<E> eventType,
        EventListener<E> eventListener) {
      return new NullEventSubscription();
    }

    @Override
    public EventSubscribtion subscribeOnAll(GlobalEventListener globalListener) {
      return new NullEventSubscription();
    }
  }

  private class NullEventPublisher<E> implements EventPublisher<E> {
    @Override
    public void publish(E event) {
    }
  }

  private class NullEventSubscription implements EventBus.EventSubscribtion {
    @Override
    public void unsubscribe() {
    }
  }
}
