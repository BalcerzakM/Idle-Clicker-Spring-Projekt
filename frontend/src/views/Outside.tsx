import { useNavigate } from "react-router-dom";
import entranceHover from "../assets/scenes/hover/entrance_hover.png";
import boxerHover from "../assets/scenes/hover/boxer_hover.png";
import securityHover from "../assets/scenes/hover/security_hover.png";
import ParkingSign from "../assets/scenes/hover/parking_sign.png";
import rankingHover from "../assets/scenes/hover/ranking_hover.png";

function Outside() {
	const navigate = useNavigate();

	return (
		<div className="outsideMain">
			<div className="outsideMain-entrance">
				<img
					src={entranceHover}
					alt="entrance_hover"
					width={187}
					height={188}
					className="hover-image"
					onClick={() => navigate("/")}
				/>
			</div>
			<p className="outsideMain-entranceTextBox">Klub</p>

			<div className="outsideMain-boxer">
				<img
					src={boxerHover}
					alt="boxer_hover"
					width={184}
					height={205}
					className="hover-image"
					onClick={() => navigate("/boxer")}
				/>
			</div>
			<p className="outsideMain-boxerTextBox">Boxer</p>
			<img
				src={ParkingSign}
				alt="parking_sign"
				className="parking_sign_image"
			/>
			<div className="outsideMain-parking-sign">
				<img
					src={ParkingSign}
					alt="parking_sign_hover"
					className="hover-image"
					onClick={() => navigate("/parking")}
				/>
			</div>
			<p className="outsideMain-parkingTextBox">Parking</p>

			<div className="outsideMain-security">
				<img
					src={securityHover}
					alt="security_hover"
					width={100}
					height={265}
					className="hover-image"
					onClick={() => navigate("/security")}
				/>
			</div>
			<p className="outsideMain-securityTextBox">Ochrona</p>

			<div className="outsideMain-ranking">
				<img
					src={rankingHover}
					alt="ranking_hover"
					width={190}
					height={228}
					className="hover-image"
					onClick={() => navigate("/ranking")}
				/>
			</div>
			<p className="outsideMain-rankingTextBox">Ranking</p>
		</div>
	);
}

export default Outside;
