# Bowling Service

##To run locally
```
./gradlew build
```
```
docker-compose up --build
```

##API
###POST localhost:8080/bowling/game
Create a game with players with a body list of player names as strings

for example ["kevin","monica"]

response example: 
```json
{
   "id": "9d8ea733-9382-48ef-8c34-c942fd55ba6a",
   "players": [
       {
           "id": "68debe46-5fda-4e9c-b5e9-9bbe12b6b2b3",
           "frames": [],
           "name": "kevin",
           "lastScoredFrame": 0
       },
       {
           "id": "287f0a6a-0955-4346-a1d7-bb827dc6f6f7",
           "frames": [],
           "name": "monica",
           "lastScoredFrame": 0
       }
   ]
}
```

###GET localhost:8080/bowling/game/{id}
Get a game

response:
```json
{
   "id": "9d8ea733-9382-48ef-8c34-c942fd55ba6a",
   "players": [
       {
           "id": "68debe46-5fda-4e9c-b5e9-9bbe12b6b2b3",
           "frames": [],
           "name": "kevin",
           "lastScoredFrame": 0
       },
       {
           "id": "287f0a6a-0955-4346-a1d7-bb827dc6f6f7",
           "frames": [],
           "name": "monica",
           "lastScoredFrame": 0
       }
   ]
}
```

###POST localhost:8080/bowling/player/{id}/frame
Add a frame to player

body:
```json
{
	"rolls": [1,9],
	"frameNumber": 1
}
```

response:
```json
{
    "id": "68debe46-5fda-4e9c-b5e9-9bbe12b6b2b3",
    "frames": [
        {
            "id": "bef17882-d510-499b-b1fb-40e9ddb02145",
            "rolls": [
                1,
                9
            ],
            "frameNumber": 1,
            "frameScore": null
        }
    ],
    "name": "kevin",
    "lastScoredFrame": 0
}
```