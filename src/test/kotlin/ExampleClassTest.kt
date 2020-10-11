import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ExampleClassTest {

    @MockK
    lateinit var annotatedMock: Injected

    @InjectMockKs
    var annotatedClass = ExampleClassWithDependency()

    @Test
    fun `Mock a class`() {
        val exampleClass = mockk<ExampleClass>()

        every { exampleClass.publicFunction() } returns "Mocked value"

        val result = exampleClass.publicFunction()

        assertEquals("Mocked value", result)
    }

    @Test
    fun `Mock a property of the class`() {
        val injected = mockk<Injected>()
        val exampleClass = ExampleClassWithDependency()
        exampleClass.injected = injected

        every { injected.multiplyByFive(any()) } returns 7

        val result = exampleClass.returnInjectedValue(10)

        assertEquals(7, result)
    }

    @Test
    fun `Example of MockKException`() {
        val exampleClass = mockk<ExampleClass>()

        exampleClass.publicFunction()
    }

    @Test
    fun `Fix MockKException using relaxed mock`() {
        val exampleClass = mockk<ExampleClass>(relaxed = true)
        val defaultStringValue = ""

        assertEquals(defaultStringValue, exampleClass.publicFunction())
    }


    @Test
    fun `Simplified property mock with annotation`() {
        every { annotatedMock.multiplyByFive(any()) } returns 7

        val result = annotatedClass.returnInjectedValue(10)

        assertEquals(7, result)
    }

    @Test
    fun `Spy a class`() {
        val exampleClass = spyk<ExampleClass>()

        assertEquals("Returned value", exampleClass.publicFunction())
    }

    @Test
    fun `Mock a private function`() {
        val exampleClass = spyk<ExampleClass>(recordPrivateCalls = true)

        every { exampleClass["privateFunction"]() } returns "Mocked value"

        assertEquals("Mocked value", exampleClass.publicFunction())
    }

    @Test
    fun `Mock an object`() {
        mockkObject(ExampleObject)

        every { ExampleObject.concat(any(), any()) } returns "Mocked value"

        val result = ExampleObject.concat("", "")
        assertEquals("Mocked value", result)
    }

    @Test
    fun `Multiple mocked instances of object`() {
        val firstMock = mockk<ExampleObject>()
        val secondMock = mockk<ExampleObject>()

        every { firstMock.concat(any(), any()) } returns "One"
        every { secondMock.concat(any(), any()) } returns "Two"

        val firstResult = firstMock.concat("", "")
        val secondResult = secondMock.concat("", "")

        assertEquals("One", firstResult)
        assertEquals("Two", secondResult)
    }

    @Test
    fun `Capture passed value`() {
        val exampleClassMock = mockk<ExampleClass>()
        val argumentSlot = slot<Int>()

        every { exampleClassMock.multiplyByTen(capture(argumentSlot)) } returns 5

        exampleClassMock.multiplyByTen(55)

        assertEquals(55, argumentSlot.captured)
    }

    @Test
    fun `Verify calls`() {
        val exampleClassMock = mockk<ExampleClass>()

        every { exampleClassMock.multiplyByTen(any()) } returns 5

        exampleClassMock.multiplyByTen(10)
        exampleClassMock.multiplyByTen(20)

        verify(exactly = 2) { exampleClassMock.multiplyByTen(any()) }
        confirmVerified(exampleClassMock)
    }
}