import StandardCurrencyImg from "../assets/other/currency_standard.png";
import PremiumCurrencyImg from "../assets/other/currency_premium.png";
import "../css/NavBarView.css";
import { useNavigate } from "react-router-dom";
import { useCharacter } from "../context/CharacterContext";
import { useState } from "react";
import ReportForm from "./ReportForm";
import InfoButton from "./InfoButton";
import useAudio from "../audio/useAudio";
import { createAudioHandlers } from "../utils/AudioHelpers";

function NavBar() {
	const navigate = useNavigate();
	const { character } = useCharacter();
	const [showReportModal, setShowReportModal] = useState(false);
	const openReportModal = () => setShowReportModal(true);
	const closeReportModal = () => setShowReportModal(false);
	const audio = useAudio();
	const { playHover, navigateWithClick, playClick } =
		createAudioHandlers(audio);

	return (
		<div className="navBar">
			<div className="navBar-character">
				<button
					type="button"
					onMouseEnter={playHover}
					onClick={() => navigateWithClick(navigate, "/player")}
				>
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
					<p>
						<b>{character?.characterClass ?? "-"}</b>, aura lvl{" "}
						<b>{character?.auraLevel ?? "-"}</b>
					</p>

					<div className="auraBar-wrapper">
						<div className="auraBar">
							<div
								className="auraBar-fill"
								style={{
									width: `${character?.levelProgressPercent ?? 0}%`,
								}}
							/>
						</div>
						<p className="navBar-character-aura-points">
							Następny level: {character?.aura ?? 0}/
							{character?.nextLevelAuraRequirement ?? 0} pkt aury
						</p>
					</div>
				</div>
			</div>
			<nav className="navBar-navigation">
				<button
					type="button"
					onMouseEnter={playHover}
					onClick={() => navigateWithClick(navigate, "/")}
				>
					Klub
				</button>
				<button
					type="button"
					onMouseEnter={playHover}
					onClick={() => navigateWithClick(navigate, "/shop")}
				>
					Szatnia
				</button>
				<button
					type="button"
					onMouseEnter={playHover}
					onClick={() => navigateWithClick(navigate, "/outside")}
				>
					Palarnia
				</button>
				<button
					type="button"
					onMouseEnter={playHover}
					onClick={() => navigateWithClick(navigate, "/ranking")}
				>
					Ranking
				</button>
				<button
					type="button"
					onMouseEnter={playHover}
					onClick={() => navigateWithClick(navigate, "/security")}
				>
					Ochrona
				</button>
				<button
					type="button"
					onMouseEnter={playHover}
					onClick={() => navigateWithClick(navigate, "/toilet")}
				>
					Toaleta
				</button>
				<button
					type="button"
					onMouseEnter={playHover}
					onClick={() => navigateWithClick(navigate, "/parking")}
				>
					Parking
				</button>
			</nav>
			<div className="navBar-logout">
				<form action="/logout" method="POST">
					<button type="submit" onClick={() => playClick}>
						WYLOGUJ SIĘ
					</button>
				</form>
			</div>
			{/* Nowy przycisk zgłoszenia */}
			<div className="navBar-report" onClick={() => playClick}>
				<button
					type="button"
					onClick={openReportModal}
					title="Zgłoś Usterkę"
					className="reportButton"
				>
					⚠️
				</button>
			</div>
			{/* Modal z formularzem */}
			{showReportModal && (
				<div className="modal-overlay" onClick={closeReportModal}>
					<div className="modal-content" onClick={(e) => e.stopPropagation()}>
						<ReportForm onClose={closeReportModal} />
					</div>
				</div>
			)}

			<InfoButton />

			<button
				onClick={() => {
					playClick;
					audio.mute(true);
				}}
			>
				Mute🔇
			</button>
		</div>
	);
}

export default NavBar;
