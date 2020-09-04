# ISA-Proj

Projekat je radjen u spring-u.
Radio sam ga u STS.
Koristio sam bazu Hibernate. Dostupna je na http://localhost:8080/h2-console.
Dize se na http://localhost:8080. Odlaskom na taj page ce korisnik biti redirektovan na "who are you?" page gde treba da bira kao koji tip korisnika zeli da se uloguje.

Profili doktora/clinicadmin-a za testiranje:

Doktor:
  Neautentifikovani (test za verifikaciju):
    Email: 	cccc@gmail.com
    Sifra: cccccccc
    Id u bazi: 1
  Autentifikovani (test za sve ostalo):
    Email: dddd@gmail.com
    Sifra: dddddddd
    Id u bazi: 2
    
Administrator Klinike:
  Neautentifikovani (test za verifikaciju):
    Email: aabb@gmail.com
    Sifra: aabbaabb
  Autentifikovani (test za sve ostalo):
    Email: abab@gmail.com
    Sifra: abcde123
   
Za testiranje autorizacije, treba se ulogovati kao doktor/clinicAdmin i ici na neki link kome moze da pristupi samo clinicAdmin/doktor (a da pritom taj link nije home/doctorHome/clinicAdminHome jer ce na tim stranicama ulogovani korisnik samo biti redirect-ovan ka svom odgovarajucem home page-u).

Postoji i funkcionalnost da, ako se prvi put doktor ili clinicAdmin loginuje, da ce morati da promeni sifru.
Da bi se testirala, treba da se udje u data.sql i promeni vrednost FIRST_LOGIN kod clinicAdmin-a ili doctor-a(preporucujem clinicAdmin sa mailom aabb@gmail.com ili doktor sa mailom dddd@gmail.com) na TRUE.
