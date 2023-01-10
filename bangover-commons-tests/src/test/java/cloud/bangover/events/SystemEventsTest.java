package cloud.bangover.events;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SystemEventsTest {
  private static final EventType<Object> EVENT_TYPE = EventType.createFor("ECHO");
  private static final Object ECHO_OBJECT = new Object();

  @Test
  public void shouldGlobalEventListenerReactOnEverySystemEventEvenIfItWasNotSetUp() {
    // Given
    MockGlobalEventListener listener = new MockGlobalEventListener();
    EventPublisher<Object> publisher = SystemEvents.publisher(EVENT_TYPE);
    EventBus.EventSubscribtion subscribtion = SystemEvents.subscribeOnAll(listener);
    // When
    publisher.publish(ECHO_OBJECT);
    subscribtion.unsubscribe();

    // Then
    Assert.assertTrue(
        listener.getHistory().hasEntry(0, new EventDescriptor<>(EVENT_TYPE, ECHO_OBJECT)));
  }

  @Test
  public void shouldReactOnSystemEventsEvenIfItWasNotSetUp() {
    // Given
    MockEventListener<Object> listener = new MockEventListener<>();
    EventPublisher<Object> publisher = SystemEvents.publisher(EVENT_TYPE);
    EventBus.EventSubscribtion subscribtion = SystemEvents.subscribeOn(EVENT_TYPE, listener);
    // When
    publisher.publish(ECHO_OBJECT);
    subscribtion.unsubscribe();
    // Then
    Assert.assertTrue(listener.getHistory().hasEntry(0, ECHO_OBJECT));
  }

  @After
  public void tearDown() {
    GlobalEvents.reset();
  }
}
