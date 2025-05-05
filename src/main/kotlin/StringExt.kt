fun String.toScreamingSnakeCase(): String {
  return this
      .replace("[^a-zA-Z0-9]".toRegex(), "_") // Replace non-alphanumeric chars with _
      .replace("(?<=[a-z])(?=[A-Z])".toRegex(), "_") // Add _ between camelCase
      .uppercase() // Convert to uppercase
      .replace("_+".toRegex(), "_") // Replace consecutive _ with single _
      .trim('_') // Remove leading/trailing _
}
