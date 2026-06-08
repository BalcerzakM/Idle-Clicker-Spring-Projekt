import StandardCurrencyImg from "../assets/other/currency_standard.png";
import PremiumCurrencyImg from "../assets/other/currency_premium.png";
import "../css/NavBarView.css";
import { useNavigate } from "react-router-dom";
import { useCharacter } from "../context/CharacterContext";

function NavBar() {
	const navigate = useNavigate();
	const { character } = useCharacter();

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
					<p>{`${character?.money ?? "Error"}`}</p>
					<img
						src={PremiumCurrencyImg}
						alt="Premium currency"
						className="currencyImg"
					/>
					<p>{`${character?.cristals ?? "Error"}`}</p>
				</div>
				<div className="navBar-character-aura">
					<p>Aura level: <b>{character?.auraLevel ?? "-"}</b></p>

					<div className="auraBar">
						<div
							className="auraBar-fill"
							style={{
								width: `${character?.levelProgressPercent ?? 0}%`,
							}}
						/>
					</div>
					<p className="navBar-character-aura-points">
						Następny level: {character?.aura ?? 0}/{character?.nextLevelAuraRequirement ?? 0} pkt aury
					</p>
				</div>
			</div>
			<nav className="navBar-navigation">
				<button type="button" onClick={() => navigate("/")}>
					Klub
				</button>
				<button type="button" onClick={() => navigate("/shop")}>
					Szatnia
				</button>
				<button type="button" onClick={() => navigate("/outside")}>
					Palarnia
				</button>
				<button type="button" onClick={() => navigate("/parking")}>
					Parking
				</button>
                <button type="button" onClick={() => navigate("/security")}>
                    Ochrona
                </button>
                <button type="button" onClick={() => navigate("/toilet")}>
                    Toaleta
                </button>
			</nav>
			<div className="navBar-logout">
				<form action="/logout" method="POST">
					<button type="submit">
						WYLOGUJ SIĘ
					</button>
				</form>
			
			</div>
		</div>
	);
}

export default NavBar;
