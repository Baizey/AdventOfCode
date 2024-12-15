@file:Suppress("UNCHECKED_CAST")

package utils.equation

interface Equation<T : Number> {
    fun clone(): Equation<T>
    fun simplify()
    fun set(name: String, value: T?)
    fun unset(name: String)
    fun unsetAll()
}

interface ResolvableEquation<T : Number> : Equation<T> {
    override fun clone(): ResolvableEquation<T>
    fun evaluate(): T
}