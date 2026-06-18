import { useNavigate } from "react-router-dom";
import barmanHover from "../assets/scenes/hover/barman_hover.png";
import szatniarzHover from "../assets/scenes/hover/szatniarz_hover.png";
import outsideHover from "../assets/scenes/hover/outside_hover.png";
import toiletHover from "../assets/scenes/hover/toilet_hover.png";

function ClubMain() {
	const navigate = useNavigate();
	return (
		<div className="clubMain">
			<div className="clubMain-barman">
				<img
					src={barmanHover}
					alt="barman_hover"
					width={141}
					height={152}
					className="hover-image"
					onClick={() => navigate("/barman")}
				/>
			</div>
			<p className="clubMain-barmanTextBox">Barman</p>

			<div className="clubMain-szatniarz">
				<img
					src={szatniarzHover}
					alt="szatniarz_hover"
					width={149}
					height={269}
					className="hover-image"
					onClick={() => navigate("/shop")}
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

			<div className="clubMain-toilet">
				<img
					src={toiletHover}
					alt="toilet_hover"
					width={108}
					height={217}
					className="hover-image"
					onClick={() => navigate("/toilet")}
				/>
			</div>
			<p className="clubMain-toiletTextBox">Toaleta</p>
		</div>
	);
}

export default ClubMain;
