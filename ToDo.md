# Introduction #

Wszystko jedno...


# Details #
## Adrian ##
  * - przykład implementacji (de)serializacji
  * - Restore + dispatcher
  * - HelloMessage
  * - przebudowa architektury

## Marcin ##
  * + dump bazy
  * - implementacja metod wirtualnych: getType(), toBinary(), fromBinary()

## Marek ##
  * - Dodać w TcpSenderze zarządzanie węzłami


## Piotr ##
  * + Cohort - sprawdzenie numeru tabeli
  * + Cohort - inkrementacja numeru tabeli
  * + DatabaseState - metody dla powyższych
  * + DatabaseState - pobranie wersji tabeli
  * + Numer tabeli przy CanCommitMessage
  * - Parsowanie wiadomosci
  * - MD5 na konkatenacje numerow wersji tabel


# Różne implementacje #
  * - ujednolicić wartości enum MessageType (wszędzie ta sama kolejność i te same stałe)
  * - serializować TransactionId (w TPCMessage), klasy potomne powinny wywoływać metodę klasy bazowej