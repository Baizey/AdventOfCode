package utils.equation

class Value<T : Number> private constructor(
    val name: String?,
    private var value: T?,
) : ResolvableEquation<T> {
    constructor(value: T) : this(null, value)
    constructor(name: String) : this(name, null)

    override fun clone(): ResolvableEquation<T> = Value(this.name, value)

    override fun evaluate(): T {
        if (value == null) throw Error("$name is not assigned")
        return value!!
    }

    override fun simplify() {}

    override fun unset(name: String) {
        if (this.name == name) this.value = null
    }

    override fun unsetAll() {
        if (name != null) this.value = null
    }

    override fun set(name: String, value: T?) {
        if (this.name == name) this.value = value
    }

    fun get(): T? = value

    override fun toString(): String = value?.toString() ?: "<$name>"
}