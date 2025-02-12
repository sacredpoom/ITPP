package p.ITPP.data

// Pokemon object

data class Pokemon(
    val id: Int,
    val name: String,
    val base_experience: Int,
    val height: Int, // In Decimeters
    val weight: Int, // In hectograms
    val abilities: List<String>, // list of potential abilities
    val forms: List<String>, // list of forms pokemon could take on
    val cry_file: String, // ogg file
    val species: String,
    val stats: Map<String, Int>, //list of base stats
    val types: List<String>, // list of types pokemon has
    val previous_evolution: String?, // Name of previous evolution or null if none
    val next_evolution: List<String>, // Name(s) of next evolution(s) or null if none
)