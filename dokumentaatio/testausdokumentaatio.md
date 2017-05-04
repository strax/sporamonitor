# Testausdokumentaatio

Tämä dokumentti korvaa erityisesti `sporamonitor.hslapi.HSLLiveClient`-luokan testit.
Luokkaa oli huomattavan vaikea testata, sillä JDK tarjoaa olemattomat mahdollisuudet matalan tason
IO:n mockaamiseen. Koska `AsyncHttpClient`-kirjaston builder-tyyppisen APIn mockaaminen ei myöskään
olisi ollut tarkoituksenmukaista (tilanteessa olisi nopeasti päästy tilanteeseen, jossa duplikoidaan kirjoitettua koodia eikä testata toiminnallisuutta), ainoa
vaihtoehto olisi ollut tehdä aitoja HTTP-kutsuja, jolloin yksikkötestien suorittaminen olisi ollut kiinni verkkoyhteyden saatavuudesta rikkoen
deterministisyyden. Luokan toiminnallisuus on kuitenkin sen verran olennainen sovelluksen toiminnallisuudelle ja toimintalogiikka sen verran yksinkertainen,
että testaamiseen riitti palautusarvon tarkistaminen debuggerilla sekä QA-tyyppinen testaus sovelluksen osalta.
