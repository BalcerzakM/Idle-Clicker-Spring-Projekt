import StandardCurrencyImg from "../assets/other/currency_standard.png";
import PremiumCurrencyImg from "../assets/other/currency_premium.png";
import "../css/NavBarView.css";
import { useNavigate } from "react-router-dom";
import { useCharacter } from "../context/CharacterContext";
import { useState, useEffect } from "react";
import SettingsModal from "./SettingsModal";
import InfoButton from "./InfoButton";
import useAudio from "../audio/useAudio";
import { createAudioHandlers } from "../utils/AudioHelpers";
import { useHelp } from "../context/HelpContext";

function NavBar() {
	const navigate = useNavigate();
	const { character } = useCharacter();
	const audio = useAudio();
	const { playHover, navigateWithClick, playClick } =
		createAudioHandlers(audio);
	const { openHelp } = useHelp(); // Pobranie funkcji otwierającej pomoc

	// Stany preferencji audio
	const [musicVolume, setMusicVolume] = useState(audio.getUserMusicVolume());
	const [effectsVolume, setEffectsVolume] = useState(audio.getEffectsVolume());
	const [muted, setMuted] = useState(audio.isMuted());

	const handleMusicVolumeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const value = parseFloat(e.target.value);
		audio.setUserMusicVolume(value);
		setMusicVolume(value);
	};

	const handleEffectsVolumeChange = (
		e: React.ChangeEvent<HTMLInputElement>,
	) => {
		const value = parseFloat(e.target.value);
		audio.setEffectsVolume(value);
		setEffectsVolume(value);
	};

	const handleToggleMute = () => {
		playClick();
		const newMuted = audio.toggleMute();
		setMuted(newMuted);
	};

	// Stan modalu ustawień
	const [showSettingsModal, setShowSettingsModal] = useState(false);
	const openSettingsModal = () => setShowSettingsModal(true);
	const closeSettingsModal = () => setShowSettingsModal(false);

	// Automatyczne otwarcie pomocy przy pierwszej wizycie
	useEffect(() => {
		const helpShown = localStorage.getItem("helpShownOnce");
		if (!helpShown) {
			openHelp(); // Bez dźwięku – to tylko pierwsze automatyczne wyświetlenie
			localStorage.setItem("helpShownOnce", "true");
		}
	}, []);

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
					onClick={() => navigateWithClick(navigate, "/drink-shop")}
				>
					Bar
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
					onClick={() => navigateWithClick(navigate, "/gang-list")}
				>
					Gangi
				</button>
			</nav>

			<div className="navBar-logout">
				<form action="/logout" method="POST">
					<button type="submit" onClick={() => playClick()}>
						WYLOGUJ SIĘ
					</button>
				</form>
			</div>

			<div className="navBar-settings">
				<button
					type="button"
					className="settingsButton"
					onClick={() => {
						playClick();
						openSettingsModal();
					}}
				>
					⚙️
				</button>
			</div>

			{showSettingsModal && (
				<SettingsModal
					onClose={closeSettingsModal}
					muted={muted}
					musicVolume={musicVolume}
					effectsVolume={effectsVolume}
					onToggleMute={handleToggleMute}
					onMusicVolumeChange={handleMusicVolumeChange}
					onEffectsVolumeChange={handleEffectsVolumeChange}
				/>
			)}

			<InfoButton />
		</div>
	);
}

export default NavBar;
