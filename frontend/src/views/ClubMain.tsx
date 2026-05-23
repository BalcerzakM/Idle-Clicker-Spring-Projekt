import barmanHover from "../assets/scenes/hover/barman_hover.png";

function ClubMain() {
	return (
		<div className="clubMain">
			<div className="clubMain-barman">
				<img
					src={barmanHover}
					alt="barman_hover"
					width={131}
					height={144}
					className="hover-image"
				/>
			</div>

			<p className="clubMain-barmanTextBox">Barman</p>
		</div>
	);
}

export default ClubMain;
