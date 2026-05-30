import StandardCurrencyImg from "../assets/other/currency_standard.png";
import PremiumCurrencyImg from "../assets/other/currency_premium.png";
import { useNavigate } from "react-router-dom";
import { useState, useEffect, useCallback } from "react";

function NavBar() {
	const navigate = useNavigate();
	const [character, setCharacter] = useState<CharacterShortInfo | null>(null);

	interface CharacterShortInfo {
		avatarPicture: string;
		money: number;
		cristals: number;
	}

	const fetchCharacterShortInfo = useCallback(async () => {
		try {
			const res = await fetch("http://localhost:8080/api/character/money");

			if (!res.ok) {
				throw new Error("Błąd pobierania danych postaci");
			}

			const heroData: CharacterShortInfo = await res.json();
			setCharacter(heroData);
		} catch (err: any) {
			console.error(err);
		}
	}, []);

	useEffect(() => {
		fetchCharacterShortInfo();
	}, [fetchCharacterShortInfo]);

	return (
		<div className="navBar">
			<div className="navBar-character">
				<button type="button" onClick={() => navigate("/player")}>
					<img
						src={`/avatars/${character?.avatarPicture}`}
						alt="Player avatar"
						className="playerAvatar"
					/>
				</button>
				<div className="navBar-character-currencies">
					<img
						src={StandardCurrencyImg}
						alt="Standard currency"
						className="currencyImg"
					/>
					<p>{`${character?.money}`}</p>
					<img
						src={PremiumCurrencyImg}
						alt="Premium currency"
						className="currencyImg"
					/>
					<p>{`${character?.cristals}`}</p>
				</div>
			</div>
			<div className="navBar-navigation">
				<button type="button" onClick={() => navigate("/")}>
					Klub
				</button>
				<button type="button" onClick={() => navigate("/shop")}>
					Szatnia
				</button>
				<button type="button" onClick={() => navigate("/outside")}>
					Palarnia
				</button>
			</div>
		</div>
	);
}

export default NavBar;
