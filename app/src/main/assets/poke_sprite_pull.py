import os
import requests

# Base URL for sprites
SPRITE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/{}.png"
POKEAPI_URL = "https://pokeapi.co/api/v2/pokemon?limit=151"
SAVE_FOLDER = "pokemon_sprites"

# Ensure directory exists
os.makedirs(SAVE_FOLDER, exist_ok=True)

# Fetch Pokémon names
response = requests.get(POKEAPI_URL)
if response.status_code != 200:
    print("Failed to fetch Pokémon data.")
    exit()

pokemon_data = response.json()["results"]

# Download each sprite
for index, pokemon in enumerate(pokemon_data, start=1):
    name = pokemon["name"]
    sprite_url = SPRITE_URL.format(index)
    
    # Download the sprite
    img_response = requests.get(sprite_url)
    if img_response.status_code == 200:
        with open(os.path.join(SAVE_FOLDER, f"{name}.png"), "wb") as file:
            file.write(img_response.content)
        print(f"Downloaded: {name}.png")
    else:
        print(f"Failed to download sprite for {name}")

print("Download complete!")
