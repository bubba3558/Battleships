# Battleships
First project in java

Typowa gra w statki. 2 Graczy uruchamia aplikacje i łączy się podając ten sam adres. 
Połączenie P2P z użyciem socketów.
Możliwość restartu po zakończeniu gry.

Aby rozpocząć grę należy ustawić 8 statków. Do testowania aplikacji można tę liczbę zmniejszyć poprzez zmianę shipsNo w Controllerze np. do 2.

Aby zbudować wystarczy użyć polecenia mvn package.
Powstanie plik o nazwie Battleships-1.0.jar w folderze target.
Grę musi uruchomić dwoję graczy, jeden tworzy nową grę, podając swój adres IP i numer pordu, drugi do
