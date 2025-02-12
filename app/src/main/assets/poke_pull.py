import requests
import json
import os

POKE_API_BASE = "https://pokeapi.co/api/v2/"
NUM_POKEMON = 151

DATA_FILE = "pokemon_data.json"
CRIES_FOLDER = "pokemon_cries"

os.makedirs(CRIES_FOLDER, exist_ok=True)

def fetch_data(url):
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()
    print(f"Failed to fetch {url}")
    return None

def download_cry(pokemon_id, cry_url):
    response = requests.get(cry_url, stream=True)
    if response.status_code == 200:
        cry_path = os.path.join(CRIES_FOLDER, f"{pokemon_id}.ogg")
        with open(cry_path, "wb") as file:
            for chunk in response.iter_content(1024):
                file.write(chunk)
        return cry_path
    print(f"Failed to download cry for Pokémon {pokemon_id}")
    return None

pokemon_list = []

for i in range(1, NUM_POKEMON + 1):
    print(f"Fetching Pokémon {i}...")

    pokemon_url = f"{POKE_API_BASE}pokemon/{i}/"
    pokemon_data = fetch_data(pokemon_url)

    if not pokemon_data:
        continue

    species_url = f"{POKE_API_BASE}pokemon-species/{i}/"
    species_data = fetch_data(species_url)

    pokemon_info = {
        "id": pokemon_data["id"],
        "name": pokemon_data["name"],
        "base_experience": pokemon_data["base_experience"],
        "height": pokemon_data["height"],
        "weight": pokemon_data["weight"],
        "abilities": [ability["ability"]["name"] for ability in pokemon_data["abilities"]],
        "forms": [form["name"] for form in pokemon_data["forms"]],
        "types": [t["type"]["name"] for t in pokemon_data["types"]],
        "stats": {stat["stat"]["name"]: stat["base_stat"] for stat in pokemon_data["stats"]},
        "species": pokemon_data["species"]["name"],
        "cry_file": None
    }

    if "cries" in pokemon_data and "latest" in pokemon_data["cries"]:
        cry_url = pokemon_data["cries"]["latest"]
        cry_path = download_cry(i, cry_url)
        if cry_path:
            pokemon_info["cry_file"] = cry_path

    pokemon_list.append(pokemon_info)

with open(DATA_FILE, "w", encoding="utf-8") as file:
    json.dump(pokemon_list, file, indent=4)

print(f"Data saved to {DATA_FILE}")
print(f"Cries saved to {CRIES_FOLDER}/")
