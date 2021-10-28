# Obligatorisk oppgave 3 i Algoritmer og Datastrukturer

Denne oppgaven er en innlevering i Algoritmer og Datastrukturer. 
Oppgaven er levert av følgende student:
* Charlie Vo, S188910, s188910@oslomet.no


# Oppgavebeskrivelse
Dyrene utvikler seg i stigene rekefølge basert på vekt.

## 🦀 Oppgave 1 
Instansierer først rotnoden for å så søke gjennom og se hvilken rettning noden skal flyttes. q tar vare på plassering p mens vi flytter på p. Ettersom vi til slutt finner node uten barn kan vi se hvilken retning den nyte noden skal opprettes på basert på størrelsen til verdien i sammenlikning med foreldrenoden.

## 🐀 Oppgave 2
Oppgaven benytter seg av Java Comparator for å sammenlikne to verdier og returnere true/false/equal. Metoden vil så gå enten til høyre eller venstre basert på dette svaret.

## 🐆 Oppgave 3
Metoden traverserer til venstre side av binærtreet til det ikke er  flere venstrnoder, så traverserer metoden høyre for å finne siste node denne retningen. Her vil da første postorder befinne seg. nestPostorden er kodet slik at den førs sjekker for rotnode så hvilken retning den skal opp eller ned for å finne neste postorden. Det opprettes en midlertidig node for å testgrave seg i tilfelle det eksisterer bladnode til høyre.

## 🐘 Oppgave 4
Det opprettes en StringJoiner for å holde teksten while- løkken skal print ut. Så itereres det gjennom og verdiene blir lagt til i Stringjoiner. I den rekrusive metoden kaller på en annen metode samtidig som den passerer gjennom to parametre som bruker for startpunkt og traversering. Fordi dette er postordre så kodes det venstr, høyre, node.

## 🐳 Oppgave 5
Det opprettes en Arraylist som skal holde alt av innholdet til alle nodene. Måten disse flyttes over er via en Deque hjelpetabell hvor høyre/venstre noder legges til. Deserialize benytter seg av en for-løkke for å legge tilbake i SBinTre klassen.

## 🦖 Oppgave 5
Mye av koden her er importert fra kompendie hvor noder omkobles om hverandre for å fjerne noder. Det har blitt kodet inn foreldrekoblinger slik at disse peker på riktig node etter fjerning. fjernAlle tar vekk alle forekomster av en verdi for å sereturnere antall av denne forekomsten. Nullstill ble kodet inn rekrusivt da dette viste seg å være den effektive metoden. Den traverserer ned til nederste node for å slette på vei oppover. Finner denne metoden er Y splitt vil den grave seg dyepre for å gjøre det samme.


