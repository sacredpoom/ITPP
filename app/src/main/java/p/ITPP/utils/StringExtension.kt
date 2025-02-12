package p.ITPP.utils

// helper for capitalizing first letter of pokemon names

fun String.capitalizeFirstLetter(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}