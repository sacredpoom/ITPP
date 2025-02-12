import requests
import json
import os

POKE_API_BASE = "https://pokeapi.co/api/v2/"
DATA_FILE = "pokemon_data.json"

def fetch_data(url):
    """Fetch data from a given URL and return the JSON response."""
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()
    print(f"Failed to fetch {url}")
    return None

def get_evolutions(pokemon_name):
    """Find previous and next evolutions for a given Pokémon name."""
    species_url = f"{POKE_API_BASE}pokemon-species/{pokemon_name}/"
    species_data = fetch_data(species_url)

    if not species_data or "evolution_chain" not in species_data:
        return None, None  # No evolution data available

    evolution_chain_url = species_data["evolution_chain"]["url"]
    evolution_data = fetch_data(evolution_chain_url)

    if not evolution_data or "chain" not in evolution_data:
        return None, None

    chain = evolution_data["chain"]
    prev_evo, next_evos = None, []

    def traverse_chain(chain, parent=None):
        """Recursive function to traverse the evolution chain."""
        nonlocal prev_evo, next_evos

        if chain["species"]["name"] == pokemon_name:
            prev_evo = parent  # Assign previous evolution
            next_evos = [evo["species"]["name"] for evo in chain["evolves_to"]]
            return True  # Stop recursion once found

        for evo in chain["evolves_to"]:
            if traverse_chain(evo, chain["species"]["name"]):
                return True  # Stop once we find the target Pokémon

        return False

    traverse_chain(chain)

    return prev_evo, next_evos if next_evos else None

# Load existing Pokémon data
if not os.path.exists(DATA_FILE):
    print(f"{DATA_FILE} not found. Run your main script first!")
    exit(1)

with open(DATA_FILE, "r", encoding="utf-8") as file:
    pokemon_list = json.load(file)

# Update each Pokémon with evolution data
for pokemon in pokemon_list:
    prev_evo, next_evo = get_evolutions(pokemon["name"])
    pokemon["previous_evolution"] = prev_evo
    pokemon["next_evolution"] = next_evo
    print(f"Updated {pokemon['name']}: prev={prev_evo}, next={next_evo}")

# Save updated data
with open(DATA_FILE, "w", encoding="utf-8") as file:
    json.dump(pokemon_list, file, indent=4)

print(f"Evolution data updated in {DATA_FILE}")
