import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.serialization.json.JsonPrimitive
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

fun JsonPrimitive.isUrl(): Boolean {
    val urlPattern = Regex("""(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]""")
    return this.isString && urlPattern.matches(content)
}

fun String.beautifyJson(): String {
    try {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = JsonParser.parseString(this)
        return gson.toJson(json)
    } catch (e: Exception) {
        e.printStackTrace()
        return this
    }
}

fun loadFile(): String {
    val fileChooser = JFileChooser()
    fileChooser.fileFilter = FileNameExtensionFilter("JSON files", "json")

    val result = fileChooser.showOpenDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        val selectedFile = fileChooser.selectedFile
        try {
            // Read the file content
            val content = selectedFile.readText()

            // Parse JSON
            return content
        } catch (e: Exception) {
            println("Error loading JSON: ${e.message}")
        }
    }
    return ""
}