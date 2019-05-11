#!/bin/bash



echo -e "\n\033[0;32m1. The user selects the day and the time when he/she would like to see the movie.\033[0m"

dates="$(date -d '6 day' +'%Y-%m-%d')T12:30,$(date -d '9 day' +'%Y-%m-%d')T16:00"

echo -e "\n\033[0;34m[GET $dates repertoire] request + response message:\033[0m\n"

data=$(curl -v http://localhost:8080/repertoire/$dates) &&

echo -e "\n\033[0;34mreceived data:\033[0m\n"
echo $data



echo -e "\n\033[0;32m2. The system lists movies available in the given time interval - title and screening times.\033[0m\n"

echo $data | python -c 'import json, sys
result = json.load(sys.stdin)
for repertoire in result["_embedded"]["repertoireList"]:
    if (repertoire["screeninged"]):
        print(repertoire["movie"]["title"], repertoire["dateTime"])' &&



echo -e "\n\033[0;32m3. The user chooses a particular screening.\033[0m"

link=$(echo $data | python -c 'import json, sys
result = json.load(sys.stdin)
for repertoire in result["_embedded"]["repertoireList"]:
    if (repertoire["screeninged"]):
        print(repertoire["_links"]["screening"]["href"])
        break') &&

echo -e "\n\033[0;34m[GET the first screening from the received list ($link)] request + response message:\033[0m\n"

data=$(curl -v $link) &&

echo -e "\n\033[0;34mreceived data:\033[0m\n"
echo $data



echo -e "\n\033[0;32m4. The system gives information regarding screening room and available seats.\033[0m\n"

echo $data | python -c 'import json, sys
result = json.load(sys.stdin)
print("screening room:", result["room"]["nr"])
print("available seats:")
for seat in result["availableSeats"]:
    print("row", seat["rowNr"], "place", seat["nr"])' &&



echo -e "\n\033[0;32m5. The user chooses seats, and gives the name of the person doing the reservation (name and surname).\033[0m"

screening=$data

seat1=$(echo $screening | python -c 'import json, sys
result = json.load(sys.stdin)
print(result["availableSeats"][0])' | tr "'" '"') &&

seat2=$(echo $screening | python -c 'import json, sys
result = json.load(sys.stdin)
print(result["availableSeats"][1])' | tr "'" '"') &&

echo -e "\n\033[0;34m[POST reservation] request + response message:\033[0m\n"

data=$(curl -v http://localhost:8080/reservations -H 'Content-type:application/json' \
-d \
    '{
        "screening": '$screening',
        "tickets": [
            {
                "ticketType": "ADULT",
                "seat": '"$seat1"'
            },
            {
                "ticketType": "STUDENT",
                "seat": '"$seat2"'
            }
        ],
        "name": "Mikołaj",
        "surname": "Wiśniewski"
    }') &&


echo -e "\n\033[0;34mreceived data:\033[0m\n"
echo $data



echo -e "\n\033[0;32m6. The system gives back the total amount to pay and reservation expiration time.\033[0m\n"

echo $data | python -c 'import json, sys
result = json.load(sys.stdin)
print("total amount to pay:", result["amountToPay"])
print("reservation expiration time:", result["expirationTime"])' &&

echo ""
