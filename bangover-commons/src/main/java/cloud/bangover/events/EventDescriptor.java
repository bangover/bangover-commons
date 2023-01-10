package cloud.bangover.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EventDescriptor<E> {
  private final EventType<E> eventType;
  private final E event;
}
