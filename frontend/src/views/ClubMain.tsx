import barmanHover from "../assets/scenes/hover/barman_hover.png";

function ClubMain() {
	return (
		<div className="clubMain">
			<div className="clubMain-barman">
				<img
					src={barmanHover}
					alt="barman_hover"
					width={126}
					height={141}
					className="hover-image"
				/>
			</div>

			<p className="clubMain-barmanTextBox">Barman</p>
		</div>
	);
}

export default ClubMain;
