package space.softsys.testfly.model

data class ApiResponse(
    val code: Int,
    val mensaje: String,
    val apps: List<App>
)

data class App(
    val id: Int,
    val name: String,
    val packageName: String,
    val version: String,
    val versionCode: Int,
    val filePath: String,
    val userId: Int,
    val createdAt: String,
    val updatedAt: String
)