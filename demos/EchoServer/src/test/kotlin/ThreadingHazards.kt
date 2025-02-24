import kotlin.test.Test
import kotlin.test.assertEquals

const val N_THREADS = 20
const val REPS = 100_000

/**
 * These tests are expected to fail because they illustrate threading hazards
 */
class ThreadingHazards {
    @Test
    fun `lost updates`() {
        val threads = mutableListOf<Thread>()
        var counter = 0
        repeat(times = N_THREADS) {
            val thread = Thread.ofPlatform().start {
                repeat(times = REPS) {
                    counter += 1
                }
            }
            threads.addLast(thread)
        }
        threads.forEach { it.join() }
        assertEquals(expected = REPS * N_THREADS, actual = counter)
    }
}