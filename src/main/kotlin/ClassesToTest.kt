class ExampleClass {

    fun multiplyByTen(number: Int) = 10 * number

    fun publicFunction() = privateFunction()

    private fun privateFunction() = "Returned value"
}

class Injected {
    fun multiplyByFive(number: Int) = 5 * number
}

class ExampleClassWithDependency {

    lateinit var injected: Injected

    fun returnInjectedValue(number: Int) = injected.multiplyByFive(number)
}