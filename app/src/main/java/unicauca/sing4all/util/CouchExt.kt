package unicauca.sing4all.util

import com.couchbase.lite.ArrayFunction
import com.couchbase.lite.Expression
import com.couchbase.lite.Function
import java.util.*

// EQUAL TO

infix fun String.equalEx(value: Any): Expression = Expression.property(this)
        .equalTo(makeExpressionValue(value))

// GREATER THAN

infix fun String.gt(value: Any): Expression = Expression.property(this)
        .greaterThan(makeExpressionValue(value))

// GREATER THAN EQUAL

infix fun String.gte(value: Any): Expression = Expression.property(this)
        .greaterThanOrEqualTo(makeExpressionValue(value))

// LESS THAN

infix fun String.lt(value: Any): Expression = Expression.property(this)
        .lessThan(makeExpressionValue(value))

// LESS THAN EQUAL

infix fun String.lte(value: Any): Expression = Expression.property(this)
        .lessThanOrEqualTo(makeExpressionValue(value))
// LIKE
infix fun String.likeEx(value: String): Expression = Function.lower(Expression.property(this))
        .like(Expression.string(value.toLowerCase()))

// LOGICAL
infix fun Expression.andEx(expression: Expression):Expression = this.and(expression)

infix fun Expression.orEx(expression: Expression):Expression = this.and(expression)

infix fun Expression.betweenEx(expressions:List<Expression>) =
        this.between(expressions[0], expressions[1])

// Array In
infix fun String.inEx(value:Any):Expression =
        ArrayFunction.contains(Expression.property(this), makeExpressionValue(value))


// Make Expression
private fun makeExpressionValue(value: Any): Expression = when(value){
    is String -> Expression.string(value)
    is Boolean -> Expression.booleanValue(value)
    is Int -> Expression.intValue(value)
    is Long -> Expression.longValue(value)
    is Date -> Expression.date(value)
    is Double -> Expression.doubleValue(value)
    is Float ->Expression.floatValue(value)
    else -> Expression.value(value)
}
