package utils.equation

import java.math.BigInteger
import kotlin.math.abs

abstract class Operation<T : Number>(
    val left: ResolvableEquation<T>,
    val right: ResolvableEquation<T>,
    private val symbol: Char,
    private val operation: (a: T, b: T) -> T,
) : ResolvableEquation<T> {
    private var result: T? = null

    abstract override fun clone(): Operation<T>

    override fun simplify() {
        left.simplify()
        right.simplify()
    }

    override fun unset(name: String) {
        left.unset(name)
        right.unset(name)
        result = null
    }

    override fun unsetAll() {
        left.unsetAll()
        right.unsetAll()
        result = null
    }

    override fun evaluate(): T {
        if (result != null) return result!!
        val a = left.evaluate()
        val b = right.evaluate()
        result = operation(a, b)
        return result!!
    }

    override fun set(name: String, value: T?) {
        left.set(name, value)
        right.set(name, value)
        result = null
    }

    override fun toString(): String = "($left $symbol $right)"
}

class Equal<T : Number>(
    left: ResolvableEquation<T>,
    right: ResolvableEquation<T>,
) : Operation<T>(left, right, '=', { _, _ -> left.evaluate() }) {
    fun verify(): Boolean = left.evaluate() == right.evaluate()
    override fun clone(): Equal<T> = Equal(left.clone(), right.clone())
}

class Neg<T : Number>(equation: ResolvableEquation<T>) : Mul<T>(Value(-1 as T), equation) {
    override fun clone(): Neg<T> = Neg(right.clone())
}

class Div<T : Number>(
    left: ResolvableEquation<T>,
    right: ResolvableEquation<T>,
) : Operation<T>(left, right, '+', { a, b ->
    when {
        a is Int && b is Int -> (a / b) as T
        a is Long && b is Long -> (a / b) as T
        a is Double && b is Double -> (a / b) as T
        a is Float && b is Float -> (a / b) as T
        else -> throw Error()
    }
}) {
    override fun clone(): Div<T> = Div(left.clone(), right.clone())
}

open class Mul<T : Number>(
    left: ResolvableEquation<T>,
    right: ResolvableEquation<T>,
) : Operation<T>(left, right, '+', { a, b ->
    when {
        a is Int && b is Int -> (a * b) as T
        a is Long && b is Long -> (a * b) as T
        a is Double && b is Double -> (a * b) as T
        a is Float && b is Float -> (a * b) as T
        else -> throw Error()
    }
}) {
    override fun clone(): Mul<T> = Mul(left.clone(), right.clone())
}

class Sub<T : Number>(
    left: ResolvableEquation<T>,
    right: ResolvableEquation<T>,
) : Operation<T>(left, right, '+', { a, b ->
    when {
        a is Int && b is Int -> (a - b) as T
        a is Long && b is Long -> (a - b) as T
        a is Double && b is Double -> (a - b) as T
        a is Float && b is Float -> (a - b) as T
        else -> throw Error()
    }
}) {
    override fun clone(): Sub<T> = Sub(left.clone(), right.clone())
}

open class Add<T : Number>(
    left: ResolvableEquation<T>,
    right: ResolvableEquation<T>,
) : Operation<T>(left, right, '+', { a, b ->
    when {
        a is Int && b is Int -> (a + b) as T
        a is Long && b is Long -> (a + b) as T
        a is Double && b is Double -> (a + b) as T
        a is Float && b is Float -> (a + b) as T
        else -> throw Error()
    }
}) {
    override fun clone(): Add<T> = Add(left.clone(), right.clone())
}