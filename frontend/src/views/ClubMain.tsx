import barmanHover from "../assets/scenes/hover/barman_hover.png";
import szatniarzHover from "../assets/scenes/hover/szatniarz_hover.png";

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

			<div className="clubMain-szatniarz">
				<img
					src={szatniarzHover}
					alt="szatniarz_hover"
					width={140}
					height={256}
					className="hover-image"
				/>
			</div>

			<p className="clubMain-szatniarzTextBox">Szatniarz</p>
		</div>
	);
}

export default ClubMain;
