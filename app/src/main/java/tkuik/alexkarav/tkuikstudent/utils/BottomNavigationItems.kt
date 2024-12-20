package tkuik.alexkarav.tkuikstudent.utils

import kotlinx.serialization.Serializable

@Serializable
data object Registration

@Serializable
data object Timetable

@Serializable
data object DocumentOrdering

@Serializable
data class DocumentOrderDetails(val orderId: Int)

@Serializable
data object Profile