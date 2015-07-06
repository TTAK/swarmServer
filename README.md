# swarmServer


network Codes

Strandard codes : 

| Code  | Name  | Variables | Size | Direction| 
| :------------: |:---------------|:-----| :---------| :---------:|
| 10      | Register |  nameSize + clientRole | int *2 | Client->Server|
|       | Client Name        |   name |nameSize | Client->Server|
| 11      | Game Parameter |  nPlayers clientId |  int *2 | Server->Client|
|       | List Client Id        |   clientId | int * nPlayers | Server->Client|
| 13      | List Client Name Size |  clientNameSize |  int * nPlayers | Server->Client|
|       | List Client Name        |   clientNames | clientNameSize | Server->Client|
| 20      | Position |  X Y Z | double * 3 | Client->Server|
| 21      | List of Position |  xPlayers yPlayers zPlayers | nPlayers * double * 3  | Server->Client|
| 23      | List of score |  scorePlayers | nPlayers * int  | Server->Client|
| 30      | Unregister |   |  | Client->Server|
| 31      | Unregister |   |  | Server->Client|
| 33      | New Player |  playerId + X Y Z of this player |  int + double * 3| Server->Client|
| 35      | New Player |  playerId + X Y Z + score + nameSize of this player  |  int * 3 + double * 3| Server->Client|
|      | New Player |  name of this player  |  nameSize | Server->Client|
| 39     | Remove Player | playerIndex  |  int | Server->Client|

Error codes :

| Code  | Name  | Variables | Size | Direction| 
| :------------: |:---------------|:-----| :---------| :---------:|
| 100      | Invalid code |  100 | int | Server->client|
