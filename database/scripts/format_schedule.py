import json 
from pathlib import Path

ParentFolder = Path(__file__).parent.parent

RawData = ParentFolder / "raw_data" / "raw_schedule_2026.json"
OutputFile = ParentFolder / "processed_data" / "schedule_2026.json"

def format_schedule(raw: Path, output: Path):
    with open(raw, "r") as file:
        data = json.load(file)

    processed = []

    for date in data.get("dates", []):
        for game in date.get("games", []):
            processed.append({
                "date": game.get("officialDate"),
                "awayTeam": game.get("teams", {}).get("away", {}).get("team", {}).get("name"),
                "homeTeam": game.get("teams", {}).get("home", {}).get("team", {}).get("name"),
                "ballparkId": game.get("venue", {}).get("id")
            })

    with open(output, "w") as file:
        json.dump(processed, file, indent=2)

    print("Games written to output")

if __name__ == "__main__":
    if not RawData.exists():
        raise FileNotFoundError(f"Raw file not found: {RawData}")
    format_schedule(RawData, OutputFile)