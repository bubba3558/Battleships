# Battleships


Typowa sieciowa gra w statki.
Do gry potrzeba 2 graczy.
Wygrywa ten, który pierwszy zestrzeli statki przeciwnika.

Rozpoczęcie gry:
Pierwszy gracz jest hostem, podaje swój numer IP i portu, na którym chce wystawić grę.
Po naciśnięciu połącz program czeka kilka minut na dołączenie drugiego gracza.

Drugi gracz dołącza poprzez podanie IP i numeru portu hosta.
Aby wybrać między zostaniem hostem a dołąćzeniem do gry, należy nacisnąć przycisk "zmień".

W każdej chwili można anulować chęć połączenia poprzez kliknięcie przyciku anuluj.
Podczas nawiązywania połączenia nie można zmienić swojej roli (host/klient).

Po powstaniu połączenia, otwiera się nowe okienko. Można rozpocząć grę

Należy ustawić wszystkie 8 statków.
Każdy statek może zostać ustawiony tylko raz.

Aby ustawić statek:
-w prawym dolnym rogu wybieramy orientacje,
-klikamy na swoją planszę, wybierając miejsce dla rufy (Będie to najwyższy i położoy najbardziej po lewo punkt statku),
-Wybieramy, który statke chcemy opstawić poprzez kliknięcie na trójkąt symbolizujący rufę.

Po zakończonej grze, można zacząć od nowa klikająć przycisk "restart".

Na górze ukazuj się wiadomości informujące o stanie gry.
Ewentualne błędy pokażą się na dole pod statkami. 

Aby zbudować wystarczy użyć polecenia mvn package z poziomu folderu z projektem.
Powstanie plik wykonywalny o nazwie Battleships-1.0.jar w podfolderze target.
