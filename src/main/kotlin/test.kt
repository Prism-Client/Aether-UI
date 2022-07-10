class test {
    data class Test(val name: String, val age: Int)

    fun hi() {
        val (name, age) = Test("hi", 4)

        Test("a", 4).copy()
    }
}