package cloud.bangover.events;

import cloud.bangover.events.EventBus.EventSubscribtion;
import cloud.bangover.events.EventBus.GlobalEventListener;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SystemEvents {
  private EventBus SYSTEM_EVENT_BUS = new LocalEventBus();

  EventBus systemEventsBus() {
    return SYSTEM_EVENT_BUS;
  }
  
  public <E> EventPublisher<E> publisher(EventType<E> eventType) {
    return systemEventsBus().getPublisher(eventType);
  }

  public <E> EventSubscribtion subscribeOn(EventType<E> eventType,
      EventListener<E> eventListener) {
    return systemEventsBus().subscribeOn(eventType, eventListener);
  }

  public EventSubscribtion subscribeOnAll(GlobalEventListener listener) {
    return systemEventsBus().subscribeOnAll(listener);
  }
}
