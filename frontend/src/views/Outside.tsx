import { useNavigate } from "react-router-dom";
import entranceHover from "../assets/scenes/hover/entrance_hover.png";
import boxerHover from "../assets/scenes/hover/boxer_hover.png";

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
		</div>
	);
}

export default Outside;
