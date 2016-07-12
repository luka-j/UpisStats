# UpisStats
Plotuje statistiku podataka s upisa u srednje škole

[Upis15Crawler](https://github.com/luq-0/Upis15Crawler) + Play! Framework + plotly

Demo: ![Example](http://i.imgur.com/RXxeoiC.png)

###Ideja
Query language koji će biti razumljiv i na srpskom (postoje engleski oblici za neke komande) i koji će biti dovoljno sličan SQLu tako da mogu da ga prevedem i izvršim bez mnogo muke. "Srce" svega je controllers.Parser koji zapravo radi to prevođenje, i vraća Listu Action-a koje u sebi sadrže tip upita, SQL query i broj osa i njihova imena, uz boju ako je tip upita PLOT.


###Parsiranje
Metode, redom:
- parse() - čita i prevodi prvi red (ose), i deli ostatak upita na linije (prema novom redu ili ;)
- parseLine(String) - radi na prvom delu reda, tj. za upit tipa `{akcija} [details...]: {šta} [uslovi...]` posmatra deo pre dvotačke, gradi početak SQL query-ja (`SELECT ...`) dok ostatak prosleđuje u parseQuery
- parseQuery(String) - tokenizuje ulaz (razdvaja na razmak stvari koje nisu pod navodnicima, spaja matematičke operacije) određuje iz koje tabele se uzimaju podaci i pod kojim uslovima, pozivajući chooseTable, chooseSubquery, parseSubquery, conjuctSubqueries
- chooseTable(String) - prevodi `[godina.]{ucenik/osnovna/smer}` u ime odgovarajuće tabele. Sve tabele sadrže kolone istog imena gde je to moguće, kako bi se olakšalo parsiranje. Kolone vezane za ocene i bodove u tabelama koje se koriste za osnovne škole i smerove su proseci odgovarajućih učenika
- chooseSubquery(String) - prevodi `{pohadjao/upisao}` u SELECT iz odgovarajuće tabele. `zeleo` je u planu, samo nisam imao vremena i ideja kako implementirati to, niti kako šta bi korisnik mogao da specifikuje (ako se u listi želja nalazi nešto, ili na kom mestu, ili da li se u listi želja nalazi škola koja ispunjava neki uslov)
- conjuctSubqueries(StringBuilder, boolean) - ispravlja zagrade i dodaje `AND`/`OR` pre `SELECT`a po potrebi
- parseSubquery(StringBuilder, List<String>, int) - pod subquery se podrazumeva svaki `SELECT` u upitu, uključujući i prvi. Određuje dokle se dati subquery proteže, radi sublist na listi tokena koja je prosleđena u drugom argumentu i poziva parseExpression 
- parseExpression(List<String>) - prevodi `i`/`ili` u `AND `/`OR `, kopira poređenja i zove parseProp da prevede sve drugo
- parseProp(String) - razdvaja matematičke operacije, prosleđuje stvari koje sam nazvao "prop", od "properties" u nedostatku boljeg imena, u "parseSingleProp". Brine se o deljenju s nulom, tako da tamo gde bi bila nula stavlja 2^30, tako da praktično količnik postavlja na broj blizak nuli, koristeći `coalesce` i `nullif`
- parseSingleProp(String) - prevodi prop u stvarno ime kolone, i to:
  - odstranjuje prefiks `int.`, `ceobroj.`, `round#n.`, `zaokruzi#n.`, `zaokrugli#n.` i zamenjuje ga odgovarajućim `ROUND`om
  - krug - krug - krug u kom se učenik upisao (1 ili 2)
  - sifra - sifra - šifra MPN za upis koju je učenik dobio
  - zelje.broj - broj_zelja - broj želja koje se nalaze na učenikovoj listi želja
  - zelje.upisana - upisana_zelja - redni broj želje koju je učenik upisao
  - bodovi.zavrsni, bodovi.zavrsni.ukupno - bodovi_sa_zavrsnog - ukupan broj bodova koji učenik nosi sa završnog, 0-30
  - bodovi.skola - bodovi_iz_skole - broj bodova koje učenik nosi iz škole na osnovu ocena u šestom, sedmom i osmom razredu, 0-70
  - bodovi.prijemni - bodovi_sa_prijemnog - ako je učenik upisao neku od škola za koju se polaže prijemni, broj bodova na tom prijemnom. Ako ne, 0
  - bodovi.matematika, bodovi.zavrsni.matematika - matematika - broj bodova na završnom testu iz matematike, 0-10
  - bodovi.srpski, bodovi.zavrsni.srpski - srpski - broj bodova na završnom testu iz srpskog, 0-10
  - bodovi.kombinovani, bodovi.zavrsni.kombinovani - kombinovani - broj bodova na kombinovanom završnom testu, 0-10
  - bodovi.takmicenja, bodovi.takmicenja.ukupno - bodovi_sa_takmicenja - broj bodova koje učenik dobija za upis na osnovu uspeha na državnim i međunarodnim takmičenjima
  - prosek, prosek.ukupno - prosek_ukupno - prosek proseka ocena u šestom, sedmom i osmom razredu, bez zaokruživanja (koliko to 64-bitni double dozvoljava)
  - 8r.prosek - prosek_osmi - prosek ocena u osmom razredu
  - 7r.prosek - prosek_sedmi - prosek ocena u sedmom razredu
  - 6r.prosek - prosek_sesti - prosek ocena u šestom razredu
  - ime, skola.ime - ime - ime škole, osnovne ili srednje (ne postoji za učenike)
  - mesto, skola.mesto - mesto - mesto u kom se nalazi škola, osnovna ili srednja, prema sajtu za upis MPN (ne postoji za učenike)
  - okrug, skola.okrug - okrug - okrug u kom se nalazi škola, osnovna ili srednja, prema šifri smera (ne postoji za učenike)
  - podrucje, smer.podrucje - podrucje - područje rada smera (ne postoji za osnovne ni za učenike)
  - smer, skola.smer, smer.ime - smer - naziv smera (ne postoji za osnovne ni za učenike)
  - kvota, skola.kvota, smer.kvota - kvota smera, tj. koliko učenika maksimalno neki smer upisuje (ne postoji za osnovne ni za učenike)
  - skola.ucenika, ucenika, ucenici.broj, skola.ucenici.broj - broj_ucenika - broj učenika koji je iz neke osnovne škole upisao srednju, odnosno broj učenika koji je upisao neki smer (ne postoji za tabelu s učenicima)
  - {8r/7r/6r}.{predmet} - {predmet}{razred} - ocena iz nekog predmeta u nekom razredu
  - prosek.{predmet} - {predmet}_p - prosek ocena iz nekog predmeta u sva tri razreda. Predmeti su: srpski, matematika, fizika, hemija (za 7r i 8r), engleski, drugiStrani, geografija, biologija, istorija, likovno, tehnicko, muzicko, fizicko, sport, vladanje
   
  
###Podaci
Podaci su preuzeti sa sajta za upis MPN prošle godine koristeći [scrapper](https://github.com/luq-0/Upis15Crawler), i biti će preuzeti ponovo kad bude objavljen raspored po školama, za ovu godinu. Unutar ovog projekta se nalazi manja verzija istog scrappera (bez nekih delova za output), u pakovanju `upismpn`.

Izlaz tog scrappera je transformisan u bazu (v. controllers.Index#populateDb), čiji se dump nalazi na [Dropboxu](https://www.dropbox.com/s/eo9fn0ux4v5zycr/upisdb120716.tar?dl=0). U pitanju je PostgreSQL baza koja se može učitati koristeći `pg_restore` kada mu prosledite taj fajl. Nazivi kolona i njihovo objašnjenje su dati u prethodnom odeljku (između prve i druge, odnosno posle druge, crtice).

Svaka tabela ima svoju klasu. Svaka godina ima svoju tabelu. Klase bez godina su obeležene kao `@MappedSupperclass` i u njima se nalaze sva primitivna polja koja nasleđuju `@Entity`-ji.

###TO-DO
Videti issues

Dokumentacija

Testiranje

Još primera je dobrodošlo

##License
GNU AGPLv3
