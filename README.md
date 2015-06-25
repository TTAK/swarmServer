# swarmServer


network Codes


| Code  | Name  | Variables | Size | Direction| 
| :------------: |:---------------|:-----| :---------| :---------:|
| 10      | Register |  nameSize| int | Client->Server|
|       | Client Name        |   name |string nameSize | Client->Server|
| 11      | Game Parameter |  nPlayers clientId | 2 * int | Server->Client|
|       | List Client Id        |   clientId | int * nPlayers | Server->Client|
| 20      | Position |  X Y Z | double * 3 | Client->Server|
| 21      | List of Poistion |  xPlayers yPlayers zPlayers | nPlayers * double * 3  | Server->Client|
| 30      | Unregister |   |  | Client->Server|
| 31      | Unregister |   |  | Server->Client|
| 33      | New Player |  playerId |  int | Server->Client|
| 35      | Remove Player | playerIndex  |  int | Server->Client|
