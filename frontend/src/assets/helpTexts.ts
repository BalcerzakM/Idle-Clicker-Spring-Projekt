export const helpTexts: Record<string, { title: string; text: string }> = {
	"/": {
		title: "Klub",
		text:
            `Witaj w klubie Śruba! 
            W grze przemieszczasz się klikając na elementy które poruszają się po najechaniu na nie kursorem, albo klikając wybraną lokację w nawigacji po lewej (nie wszystkie miejsca są w nawigacji!).
            W każdym miejscu w grze możesz kliknąć w lewym dolnym rogu ikonkę ℹ️, która otworzy ci okno z podpowiedziami do tego, gdzie obecnie jesteś. To okno możesz zamknąć klikając ❌.
            Na początek najlepiej porozglądaj się co gdzie jest klikając ruchome elementy w klubie i sprawdzając ℹ️ w różnych miejscach.
            Właśnie znajdujesz się w głównym pomieszczeniu klubu. Znajdziesz tu:
            • Szatniarza, u którego możesz kupić i sprzedać przedmioty oraz wymienić Numerki,
            • Barmana, u którego rozpoczniesz i wykonasz questy za które zyskasz Monety i Aurę,
            • Napoje obok Barmana, gdzie kupisz sobie coś do picia,
            • wejście do Toalety,
            • wyjście na Palarnię.
		`,
	},

    "/shop": {
        title: "Szatnia",
        text:
            `Tutaj możesz kupić i wyekwipować swoje przedmioty. W oknie sklepu, które znajduje się po prawej stronie, po najecheniu kursorem na jeden z czterech przedmiotów pokażą się jego statystyki oraz statystyki aktualnie założonego w tym slocie przedmiotu. 
            Wybrany przedmiot kupisz klikając "Kup". Jego miejsce w sklepie uzupełni nowa oferta. Za 1 Kryształ możesz odświeżyć ofertę sklepową klikając przycisk u góry okna sklepu. Oferta resetuje się też codziennie około godz. 6:00. 
            Przedmiot można sprzedać przeciągając go z plecaka na okno sklepu. W podobny sposób można wymieniać numerki do szatni - po przeciągnięciu numerku na okno sklepu otrzymasz przedmiot.
            Przedmiot można wyekwipować przeciągając go z plecaka na postać lub slot nad plecakiem.
            `,
    },

    "/barman": {
        title: "Barman",
        text:
            `U Barmana możesz przyjmować i wykonywać questy. Questy dzielą się na dwa główny typy:
            • Rizz Fight - polega na testowaniu swojej charyzmy, walczysz z przeciwnikiem na Rizz, a Zwinność daje bonus do ataku dla postaci u której ma wyższą wartość,
            • Strength Fight - fizyczna walka która oparta jest na  Sile, a Zwinność zwiększa szansę na unik.
            Po wybraniu questa musisz odczekać czas, który rośnie wraz twoją Aurą. W tym czasie możesz zająć się czymś innym. Pojazdy skracają czas czekania. Po zakończeniu odliczania można rozpocząć automatyczną walkę z przeciwnikiem. Po wygranej walce otrzymasz Monety i Aurę. Możesz też otrzymać Numerek do szatni, który wymienisz w Szatni na przedmiot.
            `,
    },

    "/player": {
        title: "Postać",
        text:
            `Tutaj możesz zarządzać swoim ekwipunkiem, statystykami i pojazdami.
            Po lewej stronie pokazane są twój plecak, wyposażone przez ciebie przedmioty. Najeżdżając na nie pokażą ci się ich statystyki. Przedmiot możesz założyć przeciągając go z plecaka na postać lub sloty wyżej. Niżej pokazane są aktywne efekty, które możesz dostać po użyciu napoju kupionego na Barze.
            Po prawej stronie pokazane są twoje statystyki, które możesz dodatkowo zwiększać za pomocą Kryształów (1 Kryształ = 1 pkt. statystyki). Każda statystyka odpowiada za coś innego:
            • Rizz, inaczej charyzma, odpowiada za siłę twojego ataku w walce typu Rizz Fight,
            • Siła odpowiada za siłę ataku w walce typu Strength Fight oraz daje niewielki bonus do wyniku na Boxerze,
            • Zwinność w walce na Rizz daje bonus do ataku dla postaci z większą jej wartością (większa różnica w Zwinności to większy bonus), natomiast w walce na Siłę zwiększa szansę na unik,
            • Wytrzymałość to liczba punktów życia postaci,
            • Szczęście zwiększa szansę trafienie krytyczne (podwojenie obrażeń) oraz daje większą szansę na "Lucky Punch" w Boxerze.
            Niżej pokazany pokazany jest aktualnie posiadany Pojazd wraz z terminem jego wynajmu. Pojazdy możesz wynajmować na Parkingu.
            `,
    },

	"/outside": {
		title: "Palarnia",
		text:
            `Aktualnie znajdujesz się przed wejściem do klubu. Znajdziesz tutaj:
            • wejście z powrotem do klubu,
            • minigrę Boxer, w której możesz pomnożyć swoje pieniądze (nawet przez zero!),
            • przejście na Parking,
            • Ochroniarza, u którego w wolnym czasie możesz dorobić sobie na bramkach,
            • Ranking, na którym możesz sprawdzić postęp innych graczy i zawalczyć z nimi,
            • Gang, po kliknięciu którego pokaże ci się lista Gangów.
            `,
	},

	"/boxer": {
		title: "Boxer",
        text:
            `Boxer to minigra w której możesz powiększyć lub pomniejszyć swoją fortunę. Po kliknięciu na migający wyświetlacz poniżej możesz wpisać kwotę za którą chcesz zagrać. Następnie wykonaj cios klikając gruchę po prawej stronie. Po chwili na głównym wyświetlaczu zostanie pokazany wynik twojego ciosu.
            W zależności od niego kwota będzie pomnożona przez odpowiedni mnożnik - niski wynik może pomniejszyć lub nawet wyzerować kwotę, a wyższy może ją powiększyć (nawet kilkukrotnie).
            Na wynik wpływ mają też niektóre statystyki: Szczęście zwiększa szansę na "Lucky Punch", który daje gwarantowany dobry wynik, a Siła daje niewielki bonus do wyniku.
            `,
	},

	"/parking": {
		title: "Parking",
		text:
            `Aktualnie znajdujesz się na parkingu przy klubie. Tutaj możesz:
            • kupić wybrany pojazd u Dealera Kluczyków,
            • wrócić na Palarnię.
            `,
	},

	"/security": {
		title: "Ochrona",
		text:
            `U Ochroniarza możesz sobie dorobić sobie w ochronie klubu będąc AFK. Wybierz za pomocą suwaka na ile godzin chcesz wziąć zmianę na bramkach. Po kliknięciu "Zacznij zmianę" rozpocznie się twoja zmiana.
		    W tym czasie nie możesz wykonywać żadnych questów, ani walczyć. Zmianę można przerwać przed czasem, ale nie otrzymasz wtedy wynagrodzenia. Gdy czas minie, będziesz mógł tutaj odebrać zarobione pieniądze.
	        `,
	},

    "/toilet": {
        title: "Toaleta",
        text:
            `Aktualnie znajdujesz się w klubowej toalecie. Możesz tutaj:
            • uzyskać u Podejrzanego Typa walutę premium, czyli Kryształ,
            • podjąć się walki z bossem w zadaniu specjalnym u Agenta,
            • wrócić do głównego pomieszczenia klubu.
            `,
    },

    "/premium": {
        title: "Sklep Premium",
        text:
            `Tutaj możesz uzyskać walutę premium klubu Śruba - Kryształ. Kryształy pozwalają na:
            • skracanie czasu trwania questów poprzez wynajem pojazdów,
            • odświeżanie oferty sklepu,
            • kupowanie specjalnych napojów,
            • zwiększanie statystyk w panelu postaci.
            `,
    },

    "/car-dealer": {
        title: "Dealer Kluczyków",
        text:
            `Tutaj możesz wynająć wybrany pojazd na określony czas. Każdy pojazd oferuje inne skrócenie czasu oczekiwania na ukończenie questa u Barmana. Wynajmować można za kryształy i na okres 1-7 dni, który wybiera się za pomocą suwaka. 
            Wynajem można anulować w panelu Postaci (kliknij na swój awatar).
            `,
    },

	"/ranking": {
		title: "Ranking",
        text:
            `Tutaj możesz przeglądać ranking wszystkich graczy, informacje o nich oraz walczyć z wybranym graczem. W prawym górnym rogu możesz wyszukać gracza po nazwie postaci.
            Po kliknięciu na gracza pokażą się jego statystyki, awatar oraz założone przedmioty. Pod jego ekwipunkiem znajduje się przycisk "Zaatakuj", za pomocą którego możesz zawalczyć z danym graczem by otrzymać nagrody zależne od jego poziomu Aury.
            `,
	},

    "/boss": {
        title: "Zadania Specjalne",
        text:
            `Tutaj możesz podjąć walkę z kolejnym dostępnym bossem. Bossy w przeciwieństwie do przeciwników u Barmana nie skalują się z twoją Aurą, ale za to są silniejsze. Każdego bossa możesz pokonać tylko raz i po wygraniu z jednym dostępny jest kolejny. Wygrane bossami dają lepsze nagrody w postaci większej ilości Monet, Aury oraz gwarantowany Numerek do szatni.
            `,
    },

    "/drink-shop": {
        title: "Bar",
        text:
            `Tutaj możesz nabyć serwowane w Śrubie trunki. Po prawej stronie, tak jak w Szatni, masz dostępną ofertę napojów wraz z bonusami które dają. Po kupieniu, napój pojawi się w twoim Plecaku. Możesz go wtedy użyć przeciągając go na ekwipunek lub awatar by otrzymać jego efekt na określony czas.
            Stronę karty napojów zmienisz klikając przycisk "ODWRÓĆ KARTĘ" na jej dole. Oferta napojów jest stała (bonusy skalują się z twoją Aurą).
            W jednym momencie możesz mieć maksymalnie 4 aktywne efekty oraz tylko jeden efekt tego samego napoju.
            `,
    },

    "/gang-list": {
        title: "Lista Gangów",
        text:
            `nwm wymysl cos maciek`,
    },

    "/my-gang": {
        title: "Gang",
        text:
            `nwm wymysl cos maciek v2`,
    },




};
