import json 
from pathlib import Path

ParentFolder = Path(__file__).parent.parent

RawTeamData = ParentFolder / "raw_data" / "raw_teams_2026.json"
RawBallparkData = ParentFolder / "raw_data" / "raw_ballparks_location_2026.json"
OutputFile = ParentFolder / "processed_data" / "ballparks_2026.json"

def format_schedule(rawTeam: Path, rawBallpark: Path, output: Path):
    with open(rawTeam, "r") as teamFile:
        teamData = json.load(teamFile)

    with open(rawBallpark, "r") as ballparkFile:
        ballparkData = json.load(ballparkFile)

    venueIdMap = {}

    for team in teamData.get("teams", []):
        venueIdMap[team["venue"]["name"].lower()] = {"id": team["venue"]["id"], "name": team["name"]}

    processed = []

    for venue in ballparkData.get("venues", []):
        processed.append ({
            "ballparkId": venueIdMap[venue.get("name").lower()].get("id"),
            "name": venue.get("name"),
            "teamName": venueIdMap[venue.get("name").lower()].get("name"),
            "city": venue.get("city"),
            "state": venue.get("state"),
            "country": venue.get("country"),
            "lat": venue.get("location").get("lat"),
            "lon": venue.get("location").get("lng")
        })

    with open(output, "w") as file:
        json.dump(processed, file, indent=2)

    print("Ballparks written to output")

if __name__ == "__main__":
    if not RawTeamData.exists():
        raise FileNotFoundError(f"Raw file not found: {RawTeamData}")
    if not RawBallparkData.exists():
        raise FileNotFoundError(f"Raw file not found: {RawBallparkData}")
    format_schedule(RawTeamData, RawBallparkData, OutputFile)