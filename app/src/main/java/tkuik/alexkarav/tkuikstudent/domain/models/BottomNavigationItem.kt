package tkuik.alexkarav.tkuikstudent.domain.models

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationElement(
    val itemText: String,
    val itemIconOutlined: ImageVector,
    val itemRoute: String,
    val itemIconChosen: ImageVector
)
