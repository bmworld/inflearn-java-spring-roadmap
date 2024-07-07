package memory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemoryFinderTest {
  @Test
  @DisplayName("memory finder 동작여부")
  public void getMemoryFinder() throws Exception {
    // Given

    MemoryFinder memoryFinder = new MemoryFinder();
    Memory memory = memoryFinder.get();
    System.out.println("memory = " + memory);
    assertNotNull(memory);

    // When

    // Then

  }
}
