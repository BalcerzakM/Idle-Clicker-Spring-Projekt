import { useNavigate } from "react-router-dom";
import barmanHover from "../assets/scenes/hover/barman_hover.png";
import szatniarzHover from "../assets/scenes/hover/szatniarz_hover.png";
import outsideHover from "../assets/scenes/hover/outside_hover.png";
import toiletHover from "../assets/scenes/hover/toilet_hover.png";
import { useEffect } from "react";
import useAudio from "../audio/useAudio";
import { createAudioHandlers } from "../utils/AudioHelpers";

function ClubMain() {
	const navigate = useNavigate();
	const audio = useAudio();
	const { playHover, navigateWithClick } = createAudioHandlers(audio);
	useEffect(() => {
		audio.playMusic("menu");

		return () => {
			audio.stopMusic();
		};
	}, []);

	return (
		<div className="clubMain">
			<div
				className="clubMain-barman"
				onMouseEnter={playHover}
				onClick={() => navigateWithClick(navigate, "/barman")}
			>
				<img
					src={barmanHover}
					alt="barman_hover"
					width={141}
					height={152}
					className="hover-image"
					// onClick={() => navigate("/barman")}
				/>
			</div>
			<p className="clubMain-barmanTextBox">Barman</p>

			<div
				className="clubMain-szatniarz"
				onMouseEnter={playHover}
				onClick={() => navigateWithClick(navigate, "/shop")}
			>
				<img
					src={szatniarzHover}
					alt="szatniarz_hover"
					width={149}
					height={269}
					className="hover-image"
				/>
			</div>
			<p className="clubMain-szatniarzTextBox">Szatniarz</p>

			<div className="clubMain-outside">
				<img
					src={outsideHover}
					alt="outside_hover"
					width={725}
					height={204}
					className="hover-image"
					onClick={() => navigate("/outside")}
				/>
			</div>
			<p className="clubMain-outsideTextBox">Palarnia</p>

			<div
				className="clubMain-toilet"
				onMouseEnter={playHover}
				onClick={() => navigateWithClick(navigate, "/toilet")}
			>
				<img
					src={toiletHover}
					alt="toilet_hover"
					width={108}
					height={217}
					className="hover-image"
				/>
			</div>
			<p className="clubMain-toiletTextBox">Toaleta</p>
		</div>
	);
}

export default ClubMain;
